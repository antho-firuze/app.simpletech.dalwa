package com.example.dalwaapp.auth_activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.dalwaapp.*
import com.example.dalwaapp.helper.F
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_change_pwd.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class ChangePwdActivity : AppCompatActivity() {

    private val TAG = "ChangePwdActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pwd)

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
        if (txt_pass.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Password Baru tidak boleh kosong", Toast.LENGTH_LONG).show()
            return
        }

        if (txt_pass.text.toString().length < 5) {
            Toast.makeText(applicationContext, "Panjang Password minimal 5 karakter", Toast.LENGTH_LONG).show()
            return
        }

        if (txt_pass.text.toString() != txt_new_pass_confirm.text.toString()) {
            Toast.makeText(applicationContext, "Konfirmasi Password Baru anda tidak sama", Toast.LENGTH_LONG).show()
            return
        }

        it.isEnabled = false

        val progressDialog = indeterminateProgressDialog(R.string.ajax_processing).apply { setCancelable(false) }
        progressDialog.show()

        req = setRequest(
            "auth.password_change",
            mapOf(
                "username" to session!!.username,
                "password" to txt_old_pass.text.toString(),
                "new_password" to txt_pass.text.toString()
            )
        )
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            progressDialog.dismiss()
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                Log.d(TAG, "message: "+ dataRes!!.obj().getString("message"))
                if (dataRes!!.obj().getBoolean("status")) {
                    val builder =  AlertDialog.Builder(this)
                    builder.setCancelable(false)
                    builder.setTitle("Info")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK"){ dialog, which -> finish() }
                    builder.create().show()
                } else {
                    val builder =  AlertDialog.Builder(this)
                    builder.setCancelable(false)
                    builder.setTitle("Error")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK"){ dialog, which ->
                        if (!dataRes.obj().isNull("need_login")) {
                            F().actionLogout(this)
                        }
                    }
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
