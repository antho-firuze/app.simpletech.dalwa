package com.example.DalwaApp.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import com.example.DalwaApp.*
import com.example.DalwaApp.model.tMarketUpdate
import com.example.DalwaApp.model.tPorfolioDashboard

import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_marketupdate.view.*
import kotlinx.android.synthetic.main.list_product.view.*
import kotlinx.android.synthetic.main.activity_main.*
import ss.com.bannerslider.ImageLoadingService
import ss.com.bannerslider.Slider
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

//    private var currentPage = 0
//    val timer = Timer("schedule", true)

    // LIST PRODUCT
//    data class Product(val name: String, val navperunit: String, val r1d: String)
//    fun getDataProduct(datas: ArrayList<Product>) {
//        datas.add(Product("Avrist Dana Liquid", "1,234.75", "-1.23 %"))
//        datas.add(Product("Avrist Dana LQ45", "1,234.75", "-1.50 %"))
//        datas.add(Product("Avrist Dana LQ55", "1,003.25", "-1.80 %"))
//        datas.add(Product("Avrist Dana LQ65", "1,500.53", "1.5 %"))
//        datas.add(Product("view_more", "", ""))
//        recycleView.adapter = ListAdapterProduct(products)
//    }
//    val products: ArrayList<Product> = ArrayList<Product>()
//    lateinit var adapter_product: ListAdapterProduct
//    lateinit var layoutManager_product: LinearLayoutManager

    val products: ArrayList<tPorfolioDashboard> = ArrayList()

    // LIST MARKET UPDATE
//    data class MarketUpdate(val title: String, val date: String, val img_url: String)
//    fun getDataMarketUpdate(datas: ArrayList<MarketUpdate>) {
//        datas.add(
//            MarketUpdate(
//                "Reksadana Saham dan Indeks Tertekan Aksi Profit Taking Sejak Awal 2019, Kenapa?",
//                "Today 08:30 WIB",
//                "https://picsum.photos/200/?image=0"
//            )
//        )
//        datas.add(
//            MarketUpdate(
//                "Manulife Aset Manajemen Indonesia: Tekanan ekonomi memudar, pasar makin kondusif",
//                "Today 09:30 WIB",
//                "https://picsum.photos/200/?image=10"
//            )
//        )
//        datas.add(
//            MarketUpdate(
//                "IHSG menguat tipis hingga akhir perdagangan sesi I",
//                "Today 10:30 WIB",
//                "https://picsum.photos/200/?image=20"
//            )
//        )
//        datas.add(
//            MarketUpdate(
//                "Kurs rupiah menguat ke Rp 14.208 per dollar AS",
//                "Today 12:30 WIB",
//                "https://picsum.photos/200/?image=30"
//            )
//        )
//        recycleView_MU.adapter = ListAdapter_MU(marketupdates)
//    }
//    val marketupdates: ArrayList<MarketUpdate> = ArrayList()
//    lateinit var adapter_mu: ListAdapter_MU
//    lateinit var layoutManager_mu: LinearLayoutManager

    val marketupdates: ArrayList<tMarketUpdate> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.r_nav, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        setMenuToolbar(menu)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        toolbar.setOnMenuItemClickListener {
            Toast.makeText(context, it.title, Toast.LENGTH_LONG).show()

            when (it.itemId) {
                R.id.opt_login -> {
                    val intent = Intent(context, LoginActivity::class.java)
                    context?.startActivity(intent)
                }
                R.id.opt_logout -> {
                    val builder =  AlertDialog.Builder((activity as AppCompatActivity))
                    builder.setTitle("Info")
                    builder.setMessage("Anda ingin keluar ?")
                    builder.setPositiveButton("YES"){ dialog, which ->
                        // Do something when user press the positive button
                        Toast.makeText(context,"Ok, we logout.", Toast.LENGTH_SHORT).show()

//                        isLogin = false
                        session!!.isLogin = false
                        (activity as AppCompatActivity).invalidateOptionsMenu()

                        (activity as AppCompatActivity).navigation.menu.findItem(R.id.nav_profile_wali).isVisible = false
                        (activity as AppCompatActivity).navigation.menu.findItem(R.id.nav_profile_santri).isVisible = false
                    }
                    builder.setNeutralButton("Cancel"){ dialog, which -> }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
                R.id.opt_signup -> {
                    val intent = Intent(context, RegisterActivity::class.java)
                    context?.startActivity(intent)
                }
                R.id.opt_chgpwd -> {
                    val intent = Intent(context, ChangePwdActivity::class.java)
                    context?.startActivity(intent)
                }
                R.id.opt_profile -> {
                    val intent = Intent(context, ProfileActivity::class.java)
                    context?.startActivity(intent)
                }
            }

            return@setOnMenuItemClickListener true
        }

        Slider.init(PicassoImageLoadingService())
        banner_slider1.setAdapter(MainSliderAdapter())

//        Picasso.get().load("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg").into(banner_slider1)

//        val url_img = listOf(
//            "https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg",
//            "https://assets.materialup.com/uploads/20ded50d-cc85-4e72-9ce3-452671cf7a6d/preview.jpg",
//            "https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png"
//        )
//        vp_banner?.adapter = BannerPagerAdapter(this, url_img)

//        vp_banner.adapter = BannerAdapter(childFragmentManager)
//        timer.scheduleAtFixedRate(5000, 2000) {
//            activity?.runOnUiThread { autoSlider() }
//        }

//        recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        getDataProduct(products)


        recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        var rows = realm?.where<tPorfolioDashboard>()?.findAll()
        products.addAll(Realm.getDefaultInstance().copyFromRealm(rows))
        recycleView.adapter = ListAdapterProduct(products)

//        recycleView_MU.layoutManager = LinearLayoutManager(context)
//        getDataMarketUpdate(marketupdates)

        recycleView_MU.layoutManager = LinearLayoutManager(context)
        var row2 = realm?.where<tMarketUpdate>()?.findAll()
        marketupdates.addAll(Realm.getDefaultInstance().copyFromRealm(row2))
        recycleView_MU.adapter = ListAdapter_MU(marketupdates)

        tv_viewMore.setOnClickListener {
            val intent = Intent(it.context, MarketUpdateListActivity::class.java)
            it.context.startActivity(intent)
        }
    }

    fun setMenuToolbar(menu: Menu?) {
        val opt_login = menu?.findItem(R.id.opt_login)
        val opt_signup = menu?.findItem(R.id.opt_signup)
        val opt_profile = menu?.findItem(R.id.opt_profile)
        val opt_chgpwd = menu?.findItem(R.id.opt_chgpwd)
        val opt_logout = menu?.findItem(R.id.opt_logout)

        if (session!!.isLogin) {
            opt_login!!.isVisible = false
            opt_signup!!.isVisible = false
            opt_profile!!.isVisible = true
            opt_chgpwd!!.isVisible = true
            opt_logout!!.isVisible = true
        } else {
            opt_login!!.isVisible = true
            opt_signup!!.isVisible = true
            opt_profile!!.isVisible = false
            opt_chgpwd!!.isVisible = false
            opt_logout!!.isVisible = false
        }

        (activity as AppCompatActivity).invalidateOptionsMenu()
    }

//    fun autoSlider() {
//        currentPage = if (currentPage < 3) currentPage + 1 else 0
//        vp_banner.currentItem = currentPage
//    }

//    class BannerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//        val pages = listOf(Banner1Fragment(), Banner2Fragment(), Banner3Fragment())
//
//        override fun getItem(p0: Int): Fragment {
//            return pages[p0]
//        }
//
//        override fun getCount(): Int {
//            return pages.size
//        }
//
//    }

    class MainSliderAdapter: SliderAdapter() {
        override fun getItemCount(): Int = 3

        override fun onBindImageSlide(position: Int, viewHolder: ImageSlideViewHolder?) {
            when (position){
                0 -> viewHolder?.bindImageSlide("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg")
                1 -> viewHolder?.bindImageSlide("https://assets.materialup.com/uploads/20ded50d-cc85-4e72-9ce3-452671cf7a6d/preview.jpg")
                2 -> viewHolder?.bindImageSlide("https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png")
            }
        }
    }

    class PicassoImageLoadingService: ImageLoadingService {
        override fun loadImage(url: String?, imageView: ImageView?) {
            Picasso.get().load(url).fit().into(imageView)
        }

        override fun loadImage(resource: Int, imageView: ImageView?) {
            Picasso.get().load(resource).fit().into(imageView)
        }

        override fun loadImage(url: String?, placeHolder: Int, errorDrawable: Int, imageView: ImageView?) {
            Picasso.get().load(url).fit().placeholder(placeHolder).error(errorDrawable).into(imageView)
        }
    }

//    class BannerAdapter2(fm: FragmentManager, private val banner: List<String>) : FragmentPagerAdapter(fm) {
//        override fun getItem(p0: Int): Fragment {
//            return BannerFragment.newInstance(banner[p0])
//        }
//
//        override fun getCount(): Int = banner.size
//
//    }

//    class BannerPagerAdapter1 : PagerAdapter {
//
//        var con: Context
//        var url: List<String>
//        lateinit var inflater: LayoutInflater
//
//        constructor(con: Context, url: List<String>) : super() {
//            this.con = con
//            this.url = url
//        }
//
//        override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1 as RelativeLayout
//
//        override fun getCount(): Int = url.size
//
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//            inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            var view = inflater.inflate(R.layout.fragment_banner1, container, false)
//
//            var image: ImageView = view.findViewById(R.id.img_banner)
//            Picasso.get().load(url[position]).into(image)
//
//            return view
//        }
//    }

    class ListAdapterProduct(val rows: ArrayList<tPorfolioDashboard>) :
        RecyclerView.Adapter<ListAdapterProduct.ListViewHolder>() {

        override fun getItemViewType(position: Int): Int = if (position == rows.size) R.layout.list_product_more else R.layout.list_product

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return if (p1 == R.layout.list_product)
                ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_product, p0, false))
            else
                ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_product_more, p0, false))
        }

        override fun getItemCount(): Int = rows.size + 1

        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {

            if (getItemViewType(p1) == R.layout.list_product) {
                val r = rows[p1]
                p0.itemView.apply {
                    tv_name.text = r.PortfolioNameShort
                    tv_navperunit.text = CurrFmt.format(r.NAVperUnit)
                    tv_r1d.text = PercentFmt.format(r.rYTD)

                    tv_name.setOnClickListener {
                        pubVar = mapOf("PortfolioID" to r.PortfolioID, "NAVperUnit" to r.NAVperUnit)

//                        Toast.makeText(it.context, it.tv_name.text, Toast.LENGTH_LONG).show()
                        val intent = Intent(it.context, ProductProfileActivity::class.java)

                        it.context.startActivity(intent)
                    }
                }
            } else {
                p0.itemView.apply {
                    tv_name.setOnClickListener {
                        Toast.makeText(it.context, it.tv_name.text, Toast.LENGTH_LONG).show()
                        val intent = Intent(it.context, ProductListActivity::class.java)
                        it.context.startActivity(intent)
                    }
                }
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v) {}
    }

    class ListAdapter_MU(val rows: ArrayList<tMarketUpdate>) : RecyclerView.Adapter<ListAdapter_MU.ListViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_marketupdate, p0, false))
        }

        override fun getItemCount(): Int = rows.size

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {
                tv_title.text = r.ReviewTitle
                tv_date.text = SimpleDateFormat(FORM_DATETIME_FORMAT).format(stringToDateTime(r.ReviewDate))
//                Picasso.get().load(r.img_url).fit().into(img_market)
                var resID = resources.getIdentifier("mu_${(sRandom.nextInt(20)+1).toString()}_200", "drawable", context.packageName)
                img_market.setImageResource(resID)

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
