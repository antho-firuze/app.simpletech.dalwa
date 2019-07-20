package com.example.DalwaApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_redeem.*

class RedeemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem)

        btn_reset.setOnClickListener {
            signature_pad.clear()
        }

        btn_cancel.setOnClickListener {
            val builder =  AlertDialog.Builder(this)
            builder.setTitle("Info")
            builder.setMessage("Batal Redeem ?")
            builder.setPositiveButton("YES"){ dialog, which ->
                // Do something when user press the positive button
                finish()
            }
            builder.setNeutralButton("Cancel"){ dialog, which -> }
            builder.create().show()
        }
    }
}
