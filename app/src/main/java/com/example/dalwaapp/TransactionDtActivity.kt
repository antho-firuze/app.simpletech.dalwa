package com.example.dalwaapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dalwaapp.helper.F
import com.example.dalwaapp.model.tTransaction_dt
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_transaction_dt.*
import kotlinx.android.synthetic.main.list_payment.view.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject

class TransactionDtActivity : AppCompatActivity() {

    private var rows: ArrayList<tTransaction_dt> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_dt)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        lbl_help.setOnClickListener {
            Toast.makeText(this, R.string.menu_underconstruction, Toast.LENGTH_LONG).show()
        }

        loadData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadData() {
        val progressDialog = indeterminateProgressDialog(R.string.ajax_processing).apply { setCancelable(false) }
        progressDialog.show()

        lbl_name.text = pubVar["name"].toString()
        lbl_nis.text = pubVar["nis"].toString()

        lbl_payment_status.text = pubVar["payment_status_desc"].toString()
        lbl_created_at.text = pubVar["created_at"].toString()
        lbl_payed_at.text = pubVar["payed_at"].toString()
        lbl_payment_no.text = pubVar["payment_no"].toString()
        lbl_total.text = pubVar["grand_total"].toString()
        lbl_sub_total.text = pubVar["sub_total"].toString()
        lbl_admin_charge.text = pubVar["admin_charge"].toString()
        lbl_grand_total.text = pubVar["grand_total"].toString()

        req = setRequest("payment.history_detail", mapOf("payment_id" to pubVar["payment_id"].toString()))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            progressDialog.dismiss()
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val rows = (dataRes.obj()["result"] as JSONObject).getString("rows")
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    this.rows = gson.fromJson(rows, object : TypeToken<List<tTransaction_dt>>() {}.type)

                    if (this.rows.isEmpty()) {
                        recycleView.visibility = View.GONE
                        lbl_bill_empty.visibility = View.VISIBLE
                    } else {
                        lbl_bill_empty.visibility = View.GONE
                        recycleView.visibility = View.VISIBLE
                        recycleView.layoutManager = LinearLayoutManager(this)
                        recycleView.adapter = ListAdapter(this, this.rows)
                    }

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
                }
            } else {
                snackbar(main_layout, R.string.ajax_request_failed)
            }
        }
    }

    class ListAdapter(context: Context, private val rows: ArrayList<tTransaction_dt>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_payment, p0, false))
        }

        override fun getItemCount(): Int = rows.size

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {

                lbl_desc.text = r.desc
                lbl_amount.text = currFmtID.format(r.amount)
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
