package com.example.DalwaApp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.DalwaApp.R
import com.example.DalwaApp.helper.Session
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        if (session!!.isLogin) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }

        btn_login.setOnClickListener {
            Toast.makeText(this, "Login", Toast.LENGTH_LONG).show()

            startActivity(Intent(this, MainActivity::class.java))

            session!!.isLogin = true
            finish()
        }
    }
}
