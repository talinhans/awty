package edu.uw.ischool.talin16.awty

import android.os.Bundle
import android.os.Handler
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
        if (etTimeString.isNullOrEmpty()){
            Toast.makeText(baseContext,"Cannot start because time field is empty " ,Toast.LENGTH_SHORT).show()
            return
        }
        var etTime=etTimeString?.toInt()

        if (etTime != null) {
            if (etTime <= 0) {
                Toast.makeText(baseContext,"Cannot start because time is equal to or less than 0 " ,Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            Toast.makeText(baseContext,"Cannot start because time is null " ,Toast.LENGTH_SHORT).show()
            return
        }
        if (etPhoneNumber?.length != 10) {
            Toast.makeText(baseContext,"Cannot start because digits in phone number are less than 10 " ,Toast.LENGTH_SHORT).show()
            return
        }
        if (etMessage.isNullOrEmpty()) {
            Toast.makeText(baseContext,"Cannot start because message is empty" ,Toast.LENGTH_SHORT).show()
            return
        }

        btnStart.text = "Stop"
        toggle=false

        val delay =1000*etTime*60// 1000 milliseconds == 1 second
        handler.postDelayed(object : Runnable {
            override fun run() {
                Toast.makeText(baseContext, etMessage, Toast.LENGTH_SHORT).show()
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }


}