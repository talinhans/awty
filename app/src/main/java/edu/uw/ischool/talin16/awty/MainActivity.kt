package edu.uw.ischool.talin16.awty

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnStart: Button
    var toggle = true
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var etMessage = findViewById<EditText>(R.id.etMessage)
        var etPhoneNumber = findViewById<EditText>(R.id.etPhoneNumber)
        var etTime = findViewById<EditText>(R.id.etTime)

        btnStart = findViewById<Button>(R.id.btnStart)

        btnStart.setOnClickListener {
            if (toggle) {
                validateInputAndStart(
                    etMessage.text.toString(),
                    etPhoneNumber.text.toString().trim(),
                    etTime.text.toString().trim()
                )
            } else {
                handler.removeCallbacksAndMessages(null)
                btnStart.text = "Start"
                toggle = true
            }
        }
    }

    private fun validateInputAndStart(
        etMessage: String?,
        etPhoneNumber: String?,
        etTimeString: String?
    ) {
        if (etTimeString.isNullOrEmpty()) {
            Toast.makeText(
                baseContext,
                "Cannot start because time field is empty ",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        var etTime = etTimeString?.toInt()

        if (etTime != null) {
            if (etTime <= 0) {
                Toast.makeText(
                    baseContext, "Cannot start because time is equal to or less than 0 ",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        } else {
            Toast.makeText(baseContext, "Cannot start because time is null ", Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (etPhoneNumber?.length != 10) {
            Toast.makeText(
                baseContext, "Cannot start because digits in phone number are less than 10 ",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (etMessage.isNullOrEmpty()) {
            Toast.makeText(baseContext, "Cannot start because message is empty", Toast.LENGTH_SHORT)
                .show()
            return
        }

        btnStart.text = "Stop"
        toggle = false

        val delay = 1000 * etTime * 60// 1000 milliseconds == 1 second
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Log.d("Current", "Inside Permission Check")
                    if (checkSelfPermission(android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            val smsManager: SmsManager = SmsManager.getDefault()
                            smsManager.sendTextMessage(
                                etPhoneNumber,
                                null,
                                etMessage,
                                null,
                                null
                            ) // this will send u the message

                            // Toast.makeText(baseContext, "Message Send", Toast.LENGTH_SHORT).show()

                        } catch (e: Exception) {
                            e.printStackTrace()
                            //   Log.d("Current", e.message.toString())
                        }
                    } else {
                        requestPermissions(arrayOf(android.Manifest.permission.SEND_SMS), 1)
                    }
                }
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }

}