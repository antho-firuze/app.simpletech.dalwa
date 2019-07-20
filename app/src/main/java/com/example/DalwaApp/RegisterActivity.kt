package com.example.DalwaApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.DalwaApp.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
