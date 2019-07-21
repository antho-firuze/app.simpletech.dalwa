package com.example.DalwaApp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.DalwaApp.model.tPorfolioList
import com.example.DalwaApp.model.tPortfolioListManager
import com.example.DalwaApp.model.tTimestampManager
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.list_product2.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList

class ProductListActivity : AppCompatActivity() {

    var selectedAssetType = "All"
    var selectedFundType = "All"

    // LIST PRODUCT
//    data class Product(val name: String, val navperunit: String, val r1d: String, val risk: String, val curr: String)
//    fun getDataProduct(datas: ArrayList<Product>) {
//        datas.add(Product("Avrist Dana Liquid", "1,234.75", "-1.23 %", "Konservatif", "IDR"))
//        datas.add(Product("Avrist Dana LQ45", "1,234.75", "-1.50 %", "Agresif", "IDR"))
//        datas.add(Product("Avrist Dana LQ55", "1,003.25", "-1.80 %", "Moderat", "IDR"))
//        datas.add(Product("Avrist Dana LQ65", "1,500.53", "1.5 %", "Agresif", "IDR"))
//        recycleView.adapter = ListAdapter_Product(products)
//    }
//    val products: ArrayList<Product> = ArrayList()
    var products: ArrayList<tPorfolioList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val assetType = listOf("All", "Saham", "Pendapatan Tetap", "Campuran", "Pasar Uang", "Proteksi", "Indeks")
        val assetType_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, assetType)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, assetType)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_asset_type.adapter = assetType_adapter
        sp_asset_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(parent?.context, parent?.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show()
                selectedAssetType = parent?.getItemAtPosition(position).toString()
                onFilterSelected()
            }
        }

        val fundType = listOf("All", "Syariah", "Konvensional")
        val fundType_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fundType)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, assetType)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_fund_type.adapter = fundType_adapter
        sp_fund_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(parent?.context, parent?.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show()
                selectedFundType = parent?.getItemAtPosition(position).toString()
                onFilterSelected()
            }
        }

        recycleView.layoutManager = LinearLayoutManager(this)
        var rows = Realm.getDefaultInstance().where<tPorfolioList>().findAll()
        if (rows.isEmpty()) {
            getPortfolioList()
        } else {
            products.addAll(Realm.getDefaultInstance().copyFromRealm(rows))
            recycleView.adapter = ListAdapterProduct(products)
        }
//        getDataProduct(products)

    }

    private fun onFilterSelected() {
        var rows : RealmResults<tPorfolioList>? = null
        var query = realm?.where<tPorfolioList>()

        if (selectedAssetType != "All") {
            when(selectedAssetType){
                "Saham" -> query?.equalTo("AssetTypeCode", "EQ")
                "Pendapatan Tetap" -> query?.equalTo("AssetTypeCode", "FI")
                "Campuran" -> query?.equalTo("AssetTypeCode", "MIX")
                "Pasar Uang" -> query?.equalTo("AssetTypeCode", "MM")
                "Proteksi" -> query?.equalTo("AssetTypeCode", "CPF")
                "Indeks" -> query?.equalTo("AssetTypeCode", "EQ")
            }
        }
        if (selectedFundType != "All") {

            if (selectedFundType == "Syariah")
                query?.equalTo("IsSyariah", "Y")
            else
                query?.equalTo("IsSyariah", "N")
        }

        rows = query?.findAll()!!

        if (rows.isEmpty()) {
            recycleView.visibility = View.GONE
        } else {
            recycleView.visibility = View.VISIBLE
            products = ArrayList()
            products.addAll(Realm.getDefaultInstance().copyFromRealm(rows))
            recycleView.adapter = ListAdapterProduct(products)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getPortfolioList() {
        req = setRequest("simple_fund.reksadana_daftar")
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.size > 0) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    tPortfolioListManager().insertFromJsonList((dataRes.obj()["result"] as JSONObject).getJSONArray("rows"))

                    var tt = JSONObject()
                    tt.put("PortfolioList", SimpleDateFormat(ANSI_DATETIME_FORMAT))
                    tTimestampManager().insertFromJson(tt)

                    var rows = Realm.getDefaultInstance().where<tPorfolioList>().findAll()
                    products.addAll(Realm.getDefaultInstance().copyFromRealm(rows))
                    recycleView.adapter = ListAdapterProduct(products)
//                    snackbar(main_layout, "Loading data berhasil (master)")
                } else {
//                    snackbar(main_layout, "Error Load Data: "+dataRes.obj().getString("message"))
                }
            } else {
//                snackbar(main_layout,"Error (Balance): Ajax request failed")
            }
        }
    }

    class ListAdapterProduct(val rows: ArrayList<tPorfolioList>) :
        RecyclerView.Adapter<ListAdapterProduct.ListViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_product2, p0, false))
        }

        override fun getItemCount(): Int {
            return rows.size
        }

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {
                if (p1 % 2 == 0) {
//                    layout_main.background = "@android:color/darker_gray"
                    layout_main.background = ContextCompat.getDrawable(p0.itemView.context, R.color.PowderBlue)
                }

                tv_name.text = r.PortfolioNameShort
                tv_risk.text = "(Resiko: ${r.RiskTolerance})"
                tv_curr.text = r.Ccy
                tv_navperunit.text = CurrFmt.format(r.NAVperUnit)
                tv_r1d.text = "(${PercentFmt.format(r.rYTD)})"

                tv_name.setOnClickListener {
                    pubVar = mapOf("PortfolioID" to r.PortfolioID, "NAVperUnit" to r.NAVperUnit)

//                    Toast.makeText(it.context, it.tv_name.text, Toast.LENGTH_LONG).show()
                    val intent = Intent(it.context, ProductProfileActivity::class.java)
                    it.context.startActivity(intent)
                }
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
    }

}
