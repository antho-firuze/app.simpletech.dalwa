package com.example.dalwaapp.auth_activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.example.dalwaapp.*
import com.example.dalwaapp.helper.F
import com.example.dalwaapp.helper.setSafeOnClickListener
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.txt_email
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lbl_api_url.text = URL_API

        btn_login.setSafeOnClickListener { actionDone(it) }

        btn_forgot.setSafeOnClickListener {
            startActivity(Intent(this, ForgotPwdActivity::class.java))
        }

        btn_register.setSafeOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Main2Activity::class.java))
        finish()
    }

    private fun actionDone(it: View) {
        if (! F().isValidEmail(txt_email.text.toString())) {
            Toast.makeText(applicationContext, "Alamat Email tidak valid", Toast.LENGTH_LONG).show()
            return
        }
        if (txt_pass.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Kata Sandi tidak valid", Toast.LENGTH_LONG).show()
            return
        }

        it.isEnabled = false

        val progressDialog = indeterminateProgressDialog(R.string.ajax_processing).apply { setCancelable(false) }
        progressDialog.show()

        req = setRequest(
            "auth.login",
            mapOf("username" to txt_email.text.toString(), "password" to txt_pass.text.toString())
        )
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            progressDialog.dismiss()
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    session!!.isLogin = true
                    session!!.username = txt_email.text.toString()
                    session!!.token = (dataRes.obj()["result"] as JSONObject).getString("token")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    val builder =  AlertDialog.Builder(this)
                    builder.setCancelable(false)
                    builder.setTitle("Error")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK"){ dialog, which -> }
                    builder.create().show()
                    it.isEnabled = true
                }
            } else {
                snackbar(main_layout, R.string.ajax_request_failed)
                it.isEnabled = true
            }
        }

    }
}
