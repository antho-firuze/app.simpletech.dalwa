package com.example.dalwaapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dalwaapp.helper.F
import com.example.dalwaapp.model.tBill
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.list_payment.view.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class PaymentActivity : AppCompatActivity() {

    private var bill: ArrayList<tBill> = ArrayList()
    var charge = tBill(7500.0, 999, "", 999, "Biaya Admin", Date(), 0)

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

        btn_confirm.setOnClickListener { actionDone(it) }

        loadData()
    }

    private fun actionDone(it: View) {
        it.isEnabled = false

        val progressDialog = indeterminateProgressDialog(R.string.ajax_processing).apply { setCancelable(false) }
        progressDialog.show()

        var bills: ArrayList<Int> = arrayListOf()
        bill.forEach { if (it.bill_id != 999) bills.add(it.bill_id) }

        req = setRequest(
            "payment.confirm",
            mapOf("nis" to pubVar["nis"].toString(), "payment_method_id" to 6, "bill_ids" to bills)
        )
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val accountNo = (dataRes.obj()["result"] as JSONObject).getString("account_no")

                    pubVar.put("grand_total", lbl_total.text.toString())
                    pubVar.put("account_no", accountNo)
                    startActivity(Intent(this, ConfirmActivity::class.java))
                    finish()
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setCancelable(false)
                    builder.setTitle("Error")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK") { dialog, which ->
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfimasi")
        builder.setMessage("Anda ingin membatalkan pembayaran ?")
        builder.setPositiveButton("Ya") { dialog, which ->
            super.onBackPressed()
            bill.remove(charge)
            startActivity(Intent(this, BillingActivity::class.java))
            finish()
        }
        builder.setNeutralButton("Tidak") { dialog, which -> }
        builder.create().show()
    }

    private fun loadData() {
        val progressDialog = indeterminateProgressDialog(R.string.ajax_processing).apply { setCancelable(false) }
        progressDialog.show()

        lbl_name.text = pubVar["name"].toString()
        lbl_nis.text = pubVar["nis"].toString()

        bill = pubVar["rows"] as ArrayList<tBill>

        req = setRequest("payment.setting")
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            progressDialog.dismiss()
            if (resp.data.isNotEmpty()) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    val charge_amount = (dataRes.obj()["result"] as JSONObject).getDouble("charge_amount")
                    val charge_title = (dataRes.obj()["result"] as JSONObject).getString("charge_title")
                    charge = tBill(charge_amount, 999, "", 999, charge_title, Date(), 0)
                    bill.add(charge)

                    val ttlAmount = bill.map { it.amount }.sum()
                    lbl_total.text = currFmtID.format(ttlAmount)

                    recycleView.layoutManager = LinearLayoutManager(this)
                    recycleView.adapter = ListAdapter(this, bill)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setCancelable(false)
                    builder.setTitle("Error")
                    builder.setMessage(dataRes.obj().getString("message"))
                    builder.setNeutralButton("OK") { dialog, which ->
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

    class ListAdapter(context: Context, private val rows: ArrayList<tBill>) :
        RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

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
