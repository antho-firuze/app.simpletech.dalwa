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
import com.example.dalwaapp.model.tBill
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.list_payment.view.*
import org.jetbrains.anko.design.snackbar
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class PaymentActivity : AppCompatActivity() {

    private var bill: ArrayList<tBill> = ArrayList()
    var charge = tBill(7500.0,999,"",999, "Biaya Admin", Date(), pubVar["partner_id"] as Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        lbl_detail.setOnClickListener {
            if (recycleView.visibility == View.GONE)
                recycleView.visibility = View.VISIBLE
            else
                recycleView.visibility = View.GONE
        }

        btn_confirm.setOnClickListener {
            actionDone()
        }

        loadData()
    }

    private fun actionDone() {
        var bills: ArrayList<Int> = arrayListOf()
        bill.forEach { if(it.bill_id != 999) bills.add(it.bill_id) }

        req = setRequest("payment.confirm", mapOf("nis" to pubVar["nis"].toString(), "payment_method_id" to 6, "bill_ids" to bills))
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val accountNo = (dataRes.obj()["result"] as JSONObject).getString("account_no")
                    val builder =  AlertDialog.Builder(this)
                    builder.setTitle("Info")
                    builder.setMessage(accountNo)
                    builder.setNeutralButton("OK"){ dialog, which -> }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val builder =  AlertDialog.Builder(this)
        builder.setTitle("Konfimasi")
        builder.setMessage("Anda ingin membatalkan pembayaran ?")
        builder.setPositiveButton("Ya"){ dialog, which ->
            super.onBackPressed()
            bill.remove(charge)
            finish()
        }
        builder.setNeutralButton("Tidak"){ dialog, which -> }
        builder.create().show()
    }

    private fun loadData() {
        lbl_name.text = pubVar["name"].toString()
        lbl_nis.text = pubVar["nis"].toString()

        bill = pubVar["rows"] as ArrayList<tBill>

        req = setRequest("payment.setting")
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val charge_amount = (dataRes.obj()["result"] as JSONObject).getDouble("charge_amount")
                    val charge_title = (dataRes.obj()["result"] as JSONObject).getString("charge_title")
                    charge = tBill(charge_amount,999,"",999, charge_title, Date(), pubVar["partner_id"] as Int)
                    bill.add(charge)

                    val ttlAmount = bill.map { it.amount }.sum()
                    lbl_total.text = currFmtID.format(ttlAmount)

                    recycleView.layoutManager = LinearLayoutManager(this)
                    recycleView.adapter = ListAdapter(this, bill)
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

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_payment, p0, false))
        }

        override fun getItemCount(): Int = rows.size

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {

                txt_desc.text = r.desc
                txt_amount.text = currFmtID.format(r.amount)
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
