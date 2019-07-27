package com.example.dalwaapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.txt_email
import org.jetbrains.anko.design.snackbar
import org.json.JSONObject

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            actionDone()
        }

        btn_forgot.setOnClickListener {
            startActivity(Intent(this, ForgotPwdActivity::class.java))
        }

        btn_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun actionDone() {
        req = setRequest("auth.login", mapOf("username" to txt_email.text.toString(), "password" to txt_pass.text.toString()))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
//                    val builder =  AlertDialog.Builder(this)
//                    builder.setTitle("Info")
//                    builder.setMessage(dataRes.obj().getString("message"))
//                    builder.setNeutralButton("OK"){ dialog, which ->
//                        session!!.isLogin = true
//                        session!!.token = (dataRes.obj()["result"] as JSONObject).getString("token")
//                        startActivity(Intent(this, MainActivity::class.java))
//                        finish()
//                    }
//                    builder.create().show()

                    session!!.isLogin = true
                    session!!.token = (dataRes.obj()["result"] as JSONObject).getString("token")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    val builder =  AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK"){ dialog, which -> }
                    builder.create().show()
                }
            } else {
                snackbar(main_layout,"Ajax Error: Request failed")
            }
        }
    }
}
