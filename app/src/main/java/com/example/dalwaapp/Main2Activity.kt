package com.example.dalwaapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.dalwaapp.auth_activity.LoginActivity
import kotlinx.android.synthetic.main.activity_main_2.*
import org.jetbrains.anko.design.snackbar

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        lbl_api_url.text = URL_API

        btn_pembayaran.setOnClickListener {
            if (session!!.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        btn_info.setOnClickListener {
            snackbar(main_layout, R.string.menu_underconstruction)
        }

        btn_produk.setOnClickListener {
            snackbar(main_layout, R.string.menu_underconstruction)
        }
    }
}
