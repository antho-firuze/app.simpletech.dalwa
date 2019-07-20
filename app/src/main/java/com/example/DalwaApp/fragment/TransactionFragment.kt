package com.example.DalwaApp.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.DalwaApp.R
import com.example.DalwaApp.TransactionProfileActivity
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.list_transaction.view.*
import java.util.*

class TransactionFragment : Fragment() {

    data class Transaction(
        val name: String,
        val value_percent: String,
        val unit: String,
        val navperunit: String,
        val value_amount: String,
        val cost: String,
        val profitloss: String,
        val profitloss_percent: String,
        val date: String,
        val type: String,
        val curr: String,
        val total: String,
        val status: String,
        val metode_pemb: String
    )

    fun getDataTransaction(datas: ArrayList<Transaction>) {
        datas.add(
            Transaction(
                "AVRIST ADA KAS MUTIARA",
                "68.03 %",
                "2,779,090.13",
                "1,079.80",
                "3,000,861,517.94",
                "(0.00 %) 0.00",
                "861,517.94",
                "0.03 %",
                "25-04-2018",
                "Subscription",
                "IDR",
                "3,000,000,000.00",
                "Pending",
                "Transfer Bank"
            )
        )
        datas.add(
            Transaction(
                "AVRIST DANA LQ45",
                "38.01 %",
                "2,779,090.13",
                "1,079.80",
                "3,000,861,517.94",
                "(0.00 %) 0.00",
                "861,517.94",
                "0.03 %",
                "25-05-2018",
                "Subscription",
                "IDR",
                "3,000,000,000.00",
                "Pending",
                "Transfer Bank"
            )
        )
        datas.add(
            Transaction(
                "AVRIST DANA LQ55",
                "65.05 %",
                "2,779,090.13",
                "1,079.80",
                "3,000,861,517.94",
                "(0.00 %) 0.00",
                "861,517.94",
                "0.03 %",
                "25-06-2018",
                "Subscription",
                "IDR",
                "3,000,000,000.00",
                "Selesai",
                "Transfer Bank"
            )
        )
        datas.add(
            Transaction(
                "AVRIST DANA LQ65",
                "75.06 %",
                "2,779,090.13",
                "1,079.80",
                "3,000,861,517.94",
                "(0.00 %) 0.00",
                "861,517.94",
                "0.03 %",
                "25-07-2018",
                "Subscription",
                "IDR",
                "3,000,000,000.00",
                "Pending",
                "Transfer Bank"
            )
        )
        recycleView.adapter = ListAdapter_Transaction(transactions)
    }

    val transactions: ArrayList<Transaction> = ArrayList()
    lateinit var adapter_product: ListAdapter_Transaction
    lateinit var layoutManager_transaction: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        layoutManager_transaction = LinearLayoutManager(context)
        recycleView.layoutManager = layoutManager_transaction
        getDataTransaction(transactions)

    }

    class ListAdapter_Transaction(val rows: ArrayList<Transaction>) :
        RecyclerView.Adapter<ListAdapter_Transaction.ListViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_transaction, p0, false))
        }

        override fun getItemCount(): Int {
            return rows.size
        }

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {
                tv_name.text = r.name
                tv_description.text = r.date + " " + r.type + " (" + r.curr + ")"
                tv_value_amount.text = r.value_amount
                tv_status.text = r.status
                tv_cost.text = r.cost
                tv_navperunit.text = r.navperunit
                tv_total.text = r.total
                tv_unit.text = r.unit
                tv_metode_pembayaran.text = r.metode_pemb

                btn_cancel.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Info")
                    builder.setMessage("Batalkan Transaksi ?")
                    builder.setPositiveButton("YES") { dialog, which ->
                        // Do something when user press the positive button
                        Toast.makeText(context, "Transaksi dibatalkan", Toast.LENGTH_LONG).show()
                    }
                    builder.setNeutralButton("Cancel") { dialog, which -> }
                    builder.create().show()
                }

                tv_name.setOnClickListener {
                    val intent = Intent(it.context, TransactionProfileActivity::class.java)
                    intent.putExtra("name", r.name)
                    intent.putExtra("value_amount", r.value_amount)
                    intent.putExtra("status", r.status)
                    intent.putExtra("cost", r.cost)
                    intent.putExtra("navperunit", r.navperunit)
                    intent.putExtra("total", r.total)
                    intent.putExtra("unit", r.unit)
                    intent.putExtra("metode_pemb", r.metode_pemb)
                    intent.putExtra("date", r.date)
                    intent.putExtra("type", r.type)
                    intent.putExtra("curr", r.curr)
                    it.context.startActivity(intent)
                }

            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
    }

}
