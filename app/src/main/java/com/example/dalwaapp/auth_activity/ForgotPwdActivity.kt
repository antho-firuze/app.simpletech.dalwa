package com.example.dalwaapp.auth_activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dalwaapp.R
import com.example.dalwaapp.URL_API
import com.example.dalwaapp.helper.F
import com.example.dalwaapp.req
import com.example.dalwaapp.setRequest
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class ForgotPwdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pwd)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_register, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.opt_save -> {
                actionDone(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun actionDone(it: MenuItem) {
        if (!F().isValidEmail(txt_email.text.toString())) {
            Toast.makeText(applicationContext, "Alamat Email tidak valid", Toast.LENGTH_LONG).show()
            return
        }

        it.isEnabled = false

        val progressDialog = indeterminateProgressDialog(R.string.ajax_processing).apply { setCancelable(false) }
        progressDialog.show()

        req = setRequest("auth.password_forgot", mapOf("email" to txt_email.text.toString()))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            progressDialog.dismiss()
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val builder = AlertDialog.Builder(this)
                    builder.setCancelable(false)
                    builder.setTitle("Info")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK") { dialog, which -> finish() }
                    builder.create().show()
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setCancelable(false)
                    builder.setTitle("Error")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK") { dialog, which -> }
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
