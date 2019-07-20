package com.example.DalwaApp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_marketupdate_list.*
import kotlinx.android.synthetic.main.list_marketupdate.view.*
import java.util.*

class MarketUpdateListActivity : AppCompatActivity() {

    // LIST MARKET UPDATE
    data class MarketUpdate(val title: String, val date: String, val img_url: String)
    fun getDataMarketUpdate(datas: ArrayList<MarketUpdate>) {
        datas.add(
            MarketUpdate(
                "Reksadana Saham dan Indeks Tertekan Aksi Profit Taking Sejak Awal 2019, Kenapa?",
                "Today 08:30 WIB",
                "https://picsum.photos/200/?image=0"
            )
        )
        datas.add(
            MarketUpdate(
                "Manulife Aset Manajemen Indonesia: Tekanan ekonomi memudar, pasar makin kondusif",
                "Today 09:30 WIB",
                "https://picsum.photos/200/?image=10"
            )
        )
        datas.add(
            MarketUpdate(
                "IHSG menguat tipis hingga akhir perdagangan sesi I",
                "Today 10:30 WIB",
                "https://picsum.photos/200/?image=20"
            )
        )
        datas.add(
            MarketUpdate(
                "Kurs rupiah menguat ke Rp 14.208 per dollar AS",
                "Today 12:30 WIB",
                "https://picsum.photos/200/?image=30"
            )
        )
        recycleView.adapter = ListAdapter(marketupdates)
    }
    val marketupdates: ArrayList<MarketUpdate> = ArrayList()
    lateinit var adapter: ListAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketupdate_list)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        layoutManager = LinearLayoutManager(this)
        recycleView.layoutManager = layoutManager
        getDataMarketUpdate(marketupdates)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class ListAdapter(val rows: ArrayList<MarketUpdate>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_marketupdate, p0, false))
        }

        override fun getItemCount(): Int {
            return rows.size
        }

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {
                tv_title.text = r.title
                tv_date.text = r.date
                Picasso.get().load(r.img_url).fit().into(img_market)
                tv_title.setOnClickListener {
                    val intent = Intent(it.context, MarketUpdateActivity::class.java)
//                    val intent = Intent(it.context, PerformanceChartActivity::class.java)
//                    intent.putExtra("img_url", BASE_URL_PORTFOLIO+"${row.PortfolioCode.toUpperCase()}.png")
                    it.context.startActivity(intent)
                }
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
    }

}