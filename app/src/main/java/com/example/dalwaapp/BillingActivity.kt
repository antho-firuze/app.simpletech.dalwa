package com.example.dalwaapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.util.DiffUtil
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dalwaapp.model.tBill
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_billing.*
import kotlinx.android.synthetic.main.list_bill.view.*
import org.jetbrains.anko.design.snackbar
import org.json.JSONObject
import java.text.SimpleDateFormat

var isSelectAll = true

class BillingActivity : AppCompatActivity() {

    private var bill: ArrayList<tBill> = ArrayList()
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
            recycleView.adapter = ListAdapter(this, bill)
        }

        loadData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadData() {
        lbl_name.text = pubVar["name"].toString()
        lbl_nis.text = pubVar["nis"].toString()

        req = setRequest("santri.bill", mapOf("nis" to pubVar["nis"].toString()))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.size > 0) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    var rows = (dataRes.obj()["result"] as JSONObject).getString("rows")
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    bill = gson.fromJson(rows, object : TypeToken<List<tBill>>() {}.type)

                    if (bill.isEmpty()) {
                        recycleView.visibility = View.GONE
                        lbl_bill_empty.visibility = View.VISIBLE
                    } else {
                        lbl_bill_empty.visibility = View.GONE
                        recycleView.visibility = View.VISIBLE
                        recycleView.layoutManager = LinearLayoutManager(this)
                        recycleView.adapter = ListAdapter(this, bill)
                    }

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

    class ListAdapter(context: Context, private val rows: ArrayList<tBill>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

        private val act: BillingActivity = context as BillingActivity
        private var selectedRows: ArrayList<tBill> = arrayListOf()
        var listViewHolder: ListViewHolder? = null
        var view: View? = null

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            listViewHolder = ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_bill, p0, false))
            return listViewHolder as ListViewHolder
        }

        override fun getItemCount(): Int = rows.size

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {

                txt_desc.text = r.desc
                txt_due_date.text = "Jatuh tempo: ${SimpleDateFormat(FORM_DATE_FORMAT).format(r.due_date)}"
                txt_amount.text = currFmtID.format(r.amount)
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
        }

        fun selectAll() {
            selectedRows.clear()
            selectedRows.addAll(rows)
            val ttlAmount = selectedRows.map { it.amount }.sum()
            act.lbl_total.text = currFmtID.format(ttlAmount)
        }

        fun unselectAll() {
            selectedRows.clear()
            selectedRows.removeAll(rows)
            val ttlAmount = selectedRows.map { it.amount }.sum()
            act.lbl_total.text = currFmtID.format(ttlAmount)
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
