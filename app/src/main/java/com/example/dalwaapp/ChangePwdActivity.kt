package com.example.dalwaapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_pwd.*

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class ChangePwdActivity : AppCompatActivity() {

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
                actionDone()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun actionDone() {
        Toast.makeText(applicationContext, txt_pass_old.text, Toast.LENGTH_LONG).show()
//        req = setRequest("auth.register", mapOf("full_name" to txt_name.text, "email" to txt_email.text, "phone" to txt_phone.text))
//        URL_API.httpPost().body(req).responseJson { _, resp, res ->
//            if (resp.data.size > 0) {
//                val (dataRes, _) = res
//                if (dataRes!!.obj().getBoolean("status")) {
//                    snackbar(main_layout, dataRes.obj().getString("message"))
//                    finish()
//                } else {
//                    snackbar(main_layout, "Error Load Data: "+dataRes.obj().getString("message"))
//                }
//            } else {
//                snackbar(main_layout,"Error (Balance): Ajax request failed")
//            }
//        }
    }
}
