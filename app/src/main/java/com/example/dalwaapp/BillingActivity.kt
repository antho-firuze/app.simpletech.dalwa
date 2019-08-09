package com.example.dalwaapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dalwaapp.helper.F
import com.example.dalwaapp.model.tBill
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_billing.*
import kotlinx.android.synthetic.main.list_bill.view.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject
import java.text.SimpleDateFormat

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

var isSelectAll = true
var selectedItems: ArrayList<tBill> = arrayListOf()

class BillingActivity : AppCompatActivity() {

    private var rows: ArrayList<tBill> = ArrayList()
    var adapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        cb_all.setOnClickListener {
            if (cb_all.isChecked) {
                isSelectAll = true
                adapter?.selectAll()
            } else {
                isSelectAll = false
                adapter?.unselectAll()
            }
            recycleView.layoutManager = LinearLayoutManager(this)
            recycleView.adapter = ListAdapter(this, rows)
        }

        selectedItems.clear()
        btn_payment.setOnClickListener {
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Silahkan pilih tagihan yang akan dibayar", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            pubVar.put("rows", selectedItems)
            startActivity(Intent(this, PaymentActivity::class.java))
            finish()
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

        req = setRequest("santri.bill", mapOf("nis" to pubVar["nis"].toString()))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            progressDialog.dismiss()
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val rows = (dataRes.obj()["result"] as JSONObject).getString("rows")
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    this.rows = gson.fromJson(rows, object : TypeToken<List<tBill>>() {}.type)

                    if (this.rows.isEmpty()) {
                        recycleView.visibility = View.GONE
                        lbl_bill_empty.visibility = View.VISIBLE
                    } else {
                        lbl_bill_empty.visibility = View.GONE
                        recycleView.visibility = View.VISIBLE
                        recycleView.layoutManager = LinearLayoutManager(this)
                        adapter = ListAdapter(this, this.rows)
                        recycleView.adapter = adapter
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

    class ListAdapter(context: Context, private val rows: ArrayList<tBill>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

        private val act: BillingActivity = context as BillingActivity
        private var selectedRows: ArrayList<tBill> = arrayListOf()

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_bill, p0, false))
        }

        override fun getItemCount(): Int = rows.size

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {

                lbl_desc.text = r.desc
                txt_due_date.text = "Jatuh tempo: ${SimpleDateFormat(FORM_DATE_FORMAT).format(r.due_date)}"
                lbl_amount.text = currFmtID.format(r.amount)
                cb_sel.tag = r.amount

                cb_sel.setOnClickListener {

                    checkSelection(p0.itemView, r)
                }

                cb_sel.isChecked = isSelectAll
                checkSelection(p0.itemView, r)
            }
        }

        private fun checkSelection(view: View, r: tBill) {
            if (view.cb_sel.isChecked) {
                selectedRows.add(r)
            } else {
                selectedRows.remove(r)
            }

            val ttlAmount = selectedRows.map { it.amount }.sum()
            act.lbl_total.text = currFmtID.format(ttlAmount)
            act.cb_all.isChecked = rows.size == selectedRows.size

            selectedItems = selectedRows
        }

        fun selectAll() {
            selectedRows.clear()
            selectedRows.addAll(rows)
            val ttlAmount = selectedRows.map { it.amount }.sum()
            act.lbl_total.text = currFmtID.format(ttlAmount)

            selectedItems = selectedRows
        }

        fun unselectAll() {
            selectedRows.clear()
            selectedRows.removeAll(rows)
            val ttlAmount = selectedRows.map { it.amount }.sum()
            act.lbl_total.text = currFmtID.format(ttlAmount)

            selectedItems = selectedRows
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
