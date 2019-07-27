package com.example.dalwaapp

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dalwaapp.helper.F
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.design.snackbar

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
                actionDone()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun actionDone() {
        if (! F().isValidEmail(txt_email.text.toString())) {
            Toast.makeText(applicationContext, "Alamat Email tidak valid", Toast.LENGTH_LONG).show()
            return
        }

        req = setRequest("auth.password_forgot", mapOf("email" to txt_email.text.toString()))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val builder =  AlertDialog.Builder(this)
                    builder.setTitle("Info")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK"){ dialog, which -> finish() }
                    builder.create().show()
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
