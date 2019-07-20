package com.example.DalwaApp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.DalwaApp.model.tPorfolio
import com.example.DalwaApp.product.ProductEfekFragment
import com.example.DalwaApp.product.ProductInfoFragment
import com.example.DalwaApp.product.ProductPerformanceFragment
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_product_profile.*

class ProductProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_profile)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.addFragment(ProductInfoFragment(), "Informasi")
        adapter.addFragment(ProductPerformanceFragment(), "Kinerja")
        adapter.addFragment(ProductEfekFragment(), "Efek")
        vp_main.adapter = adapter
        tabs_main.setupWithViewPager(vp_main)

//        vp_main.adapter = MyPagerAdapter(supportFragmentManager)
//        tabs_main.setupWithViewPager(vp_main)

        btn_subscription.setOnClickListener {
            val intent = Intent(it.context, SubscriptionActivity::class.java)
            intent.putExtra("PortfolioID", pubVar?.get("PortfolioID") as Int)
            intent.putExtra("name", tv_name.text)
            intent.putExtra("asset_type", tv_type.text)
            intent.putExtra("risk_type", tv_risk.text)
            intent.putExtra("subs_min", 10000)
            it.context.startActivity(intent)
        }

        getIncomingIntent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getIncomingIntent() {
        var row = realm?.where<tPorfolio>()?.equalTo("PortfolioID", pubVar?.get("PortfolioID") as Int)?.findFirst()

        tv_name.text = row?.PortfolioNameShort      // pubVar.get("PortfolioName")?.toString()
        tv_type.text = row?.AssetTypeDescription    // pubVar.get("AssetTypeDescription")?.toString()
        tv_risk.text = row?.RiskTolerance           // pubVar.get("RiskTolerance")?.toString()

//        if (intent.hasExtra("PortfolioID")) {
//            PortfolioID = intent.getIntExtra("PortfolioID", 0)
//            tv_name.text = intent.getStringExtra("PortfolioName")
//            tv_type.text = intent.getStringExtra("AssetTypeDescription")
//            tv_risk.text = intent.getStringExtra("RiskTolerance")
//            tv_nav_per_unit.text = intent.getStringExtra("NAVperUnit")
//            tv_PositionDate.text = intent.getStringExtra("PositionDate")
//            tv_1d.text = intent.getStringExtra("r1D")
//            tv_mtd.text = intent.getStringExtra("rMTD")
//            tv_ytd.text = intent.getStringExtra("rYTD")
//            tv_1mo.text = intent.getStringExtra("r1Mo")
//            tv_3mo.text = intent.getStringExtra("r3Mo")
//            tv_6mo.text = intent.getStringExtra("r6Mo")
//            tv_1y.text = intent.getStringExtra("r1Y")
//            tv_2y.text = intent.getStringExtra("r2Y")
//            tv_5y.text = intent.getStringExtra("r5Y")
//            getPerformanceChartData(PortfolioID)
//        }
    }

    class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragmentList : MutableList<Fragment> = ArrayList()
        private val titleList : MutableList<String> = ArrayList()

        override fun getItem(p0: Int): Fragment {
            return fragmentList[p0]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            titleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

    }

//    class MyPagerAdapter_old(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//
//        val pages = listOf(
//            ProductInfoFragment(),
//            ProductPerformanceFragment(),
//            ProductEfekFragment()
//        )
//
//        override fun getItem(p0: Int): Fragment {
//            return pages[p0]
//        }
//
//        override fun getCount(): Int {
//            return pages.size
//        }
//
//        override fun getPageTitle(position: Int): CharSequence? {
//            return when(position) {
//                0 -> "Informasi"
//                1 -> "Kinerja"
//                else -> "Efek"
//            }
//        }
//
//    }

}
