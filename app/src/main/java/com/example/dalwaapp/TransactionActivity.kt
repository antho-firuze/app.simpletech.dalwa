package com.example.dalwaapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dalwaapp.helper.F
import com.example.dalwaapp.model.tTransaction
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.list_transaction.view.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject
import java.text.SimpleDateFormat

class TransactionActivity : AppCompatActivity() {

    private var rows: ArrayList<tTransaction> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

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

        req = setRequest("payment.history", mapOf("nis" to pubVar["nis"].toString()))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            progressDialog.dismiss()
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val rows = (dataRes.obj()["result"] as JSONObject).getString("rows")
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    this.rows = gson.fromJson(rows, object : TypeToken<List<tTransaction>>() {}.type)

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

    class ListAdapter(ctx: Context, private val rows: ArrayList<tTransaction>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_transaction, p0, false))
        }

        override fun getItemCount(): Int = rows.size

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {

                lbl_payment_status.text = r.payment_status_desc
                lbl_payment_no.text = r.payment_no
                lbl_created_date.text = "${SimpleDateFormat(FORM_0_DATETIME_FORMAT).format(F().stringToDateTime(r.created_at))} WIB"
                lbl_grand_total.text = currFmtID.format(r.grand_total)

                when (r.payment_status_id) {
                    1 -> layout_color.background = ContextCompat.getDrawable(p0.itemView.context, R.color.red_300)
                    2 -> layout_color.background = ContextCompat.getDrawable(p0.itemView.context, R.color.green_300)
                    else -> layout_color.background = ContextCompat.getDrawable(p0.itemView.context, R.color.grey_300)
                }

                layout_item.setOnClickListener {
                    pubVar.put("payment_id", r.payment_id)
                    pubVar.put("payment_status_desc", r.payment_status_desc)
                    pubVar.put("created_at", "${SimpleDateFormat(FORM_0_DATETIME_FORMAT).format(F().stringToDateTime(r.created_at))} WIB")
                    if (r.payed_at.isNullOrEmpty())
                        pubVar.put("payed_at", "-")
                    else
                        pubVar.put("payed_at", "${SimpleDateFormat(FORM_0_DATETIME_FORMAT).format(F().stringToDateTime(r.payed_at))} WIB")
                    pubVar.put("payment_no", r.payment_no)
                    pubVar.put("grand_total", currFmtID.format(r.grand_total))
                    pubVar.put("sub_total", currFmtID.format(r.sub_total))
                    pubVar.put("admin_charge", currFmtID.format(r.admin_charge))

                    context?.startActivity(Intent(context, TransactionDtActivity::class.java))
                }
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
