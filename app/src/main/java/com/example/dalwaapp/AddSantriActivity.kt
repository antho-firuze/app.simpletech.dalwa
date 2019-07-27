package com.example.dalwaapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.dalwaapp.helper.F
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_add_santri.*
import kotlinx.android.synthetic.main.activity_register.main_layout
import kotlinx.android.synthetic.main.activity_register.toolbar
import kotlinx.android.synthetic.main.fragment_santri.btn_add
import org.jetbrains.anko.design.snackbar
import org.json.JSONObject

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class AddSantriActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_santri)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        btn_add.setOnClickListener { actionDone() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun actionDone() {
        if (txt_nis.text.toString().equals("")) {
            Toast.makeText(applicationContext, "Nomor Induk Santri/Siswa tidak valid", Toast.LENGTH_LONG).show()
            return
        }

        req = setRequest("wali.add_santri", mapOf("nis" to txt_nis.text.toString()))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val full_name = (dataRes.obj()["result"] as JSONObject).getString("full_name")
                    val msg = dataRes.obj().getString("message")

                    val builder =  AlertDialog.Builder(this)
                    builder.setTitle("Info")
                    builder.setMessage("${msg}\r\n\r\nNIS: ${txt_nis.text}\r\nNama: ${full_name}")
                    builder.setNeutralButton("OK"){ dialog, which ->
                        isRefresh = true
                        finish()
                    }
                    builder.create().show()
                } else {
                    val need_login = dataRes.obj().isNull("need_login")

                    val builder =  AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK"){ dialog, which ->
                        if (!dataRes.obj().isNull("need_login")) {
                            F().actionLogout()
                            startActivity(Intent(this, LoginActivity::class.java))
                            MainActivity().finish()
                            finish()
                        }
                    }
                    builder.create().show()
                }
            } else {
                snackbar(main_layout,"Ajax Error: Request failed")
            }
        }
    }

}
