package com.example.DalwaApp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import com.example.DalwaApp.R
import com.example.DalwaApp.RedeemActivity
import com.example.DalwaApp.SubscriptionActivity
import com.github.mikephil.charting.data.PieDataSet
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.android.synthetic.main.list_ownership_fund.view.*
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieEntry



class PortfolioFragment : Fragment() {

    data class Fund(
        val name: String,
        val value_percent: String,
        val unit: String,
        val navperunit: String,
        val value_amount: String,
        val cost: String,
        val profitloss: String,
        val profitloss_percent: String,
        val date: String,
        val asset_type: String,
        val risk_type: String,
        val subs_min: Number
    )

    fun getDataFund(datas: ArrayList<Fund>) {
        datas.add(Fund("AVRIST ADA KAS MUTIARA", "68.03 %", "2,779,090.13", "1,079.80", "3,000,861,517.94", "3,000,000,000.00", "861,517.94", "0.03 %", "25-Jul-2018", "Pasar Uang", "Konservatif", 10000))
        datas.add(Fund("AVRIST DANA LQ45", "38.01 %", "2,779,090.13", "1,079.80", "3,000,861,517.94", "3,000,000,000.00", "861,517.94", "0.03 %", "25-Jul-2018", "Pasar Uang", "Konservatif", 10000))
        datas.add(Fund("AVRIST DANA LQ55", "65.05 %", "2,779,090.13", "1,079.80", "3,000,861,517.94", "3,000,000,000.00", "861,517.94", "0.03 %", "25-Jul-2018", "Pasar Uang", "Konservatif", 10000))
        datas.add(Fund("AVRIST DANA LQ65", "75.06 %", "2,779,090.13", "1,079.80", "3,000,861,517.94", "3,000,000,000.00", "861,517.94", "0.03 %", "25-Jul-2018", "Pasar Uang", "Konservatif", 10000))
        recycleView.adapter = ListAdapter_Fund(funds)
    }

    val funds: ArrayList<Fund> = ArrayList()
    lateinit var adapter_product: ListAdapter_Fund
    lateinit var layoutManager_fund: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadPieChart()

        layoutManager_fund = LinearLayoutManager(context)
        recycleView.layoutManager = layoutManager_fund
        getDataFund(funds)

        val assetType = listOf("IDR", "USD")
        val assetType_adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, assetType)
        sp_curr.adapter = assetType_adapter


    }

    fun loadPieChart() {
        val yVals = ArrayList<PieEntry>()
        yVals.add(PieEntry(68.03f, "AAKAMU (68.03 %)"))
        yVals.add(PieEntry(20.58f, "ASAS (20.58 %)"))
        yVals.add(PieEntry(11.39f, "SPIRIT4 (11.39 %)"))

        val dataSet = PieDataSet(yVals, "")
        dataSet.valueTextSize=0f
        dataSet.setDrawValues(false)
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.MAGENTA)
        colors.add(Color.GREEN)
        colors.add(Color.BLUE)

        dataSet.setColors(colors)
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.setDrawSliceText(false)
        pieChart.centerTextRadiusPercent = 0f
        pieChart.isDrawHoleEnabled = true
        pieChart.legend.isEnabled = true
        pieChart.legend.position = Legend.LegendPosition.ABOVE_CHART_CENTER
        pieChart.description.isEnabled = false
    }

    class ListAdapter_Fund(val rows: ArrayList<Fund>) : RecyclerView.Adapter<ListAdapter_Fund.ListViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_ownership_fund, p0, false))
        }

        override fun getItemCount(): Int {
            return rows.size
        }

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {
                tv_name.text = r.name
                tv_value_percent.text = r.value_percent
                tv_unit.text = r.unit
                tv_navperunit.text = r.navperunit
                tv_value_amount.text = r.value_amount
                tv_cost.text = r.cost
                tv_profitloss.text = r.profitloss
                tv_profitloss_percent.text = r.profitloss_percent
                tv_date.text = r.date

                btn_subscription.setOnClickListener {
                    val intent = Intent(it.context, SubscriptionActivity::class.java)
                    intent.putExtra("name", r.name)
                    intent.putExtra("asset_type", r.asset_type)
                    intent.putExtra("risk_type", r.risk_type)
                    intent.putExtra("subs_min", r.subs_min)
                    it.context.startActivity(intent)
                }

                btn_redeem.setOnClickListener {
                    val intent = Intent(it.context, RedeemActivity::class.java)
                    intent.putExtra("name", r.name)
                    intent.putExtra("asset_type", r.asset_type)
                    intent.putExtra("unit", r.unit)
                    intent.putExtra("value_amount", r.value_amount)
                    it.context.startActivity(intent)
                }
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
    }

}
