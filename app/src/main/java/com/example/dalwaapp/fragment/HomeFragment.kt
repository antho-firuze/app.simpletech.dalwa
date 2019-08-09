package com.example.dalwaapp.fragment

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
import com.example.dalwaapp.*
import com.example.dalwaapp.auth_activity.ChangePwdActivity
import com.example.dalwaapp.auth_activity.LoginActivity
import com.example.dalwaapp.auth_activity.RegisterActivity
import com.example.dalwaapp.helper.F
import com.example.dalwaapp.model.tNews
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_news.view.*
import ss.com.bannerslider.ImageLoadingService
import ss.com.bannerslider.Slider
import ss.com.bannerslider.adapters.SliderAdapter
import ss.com.bannerslider.viewholder.ImageSlideViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_home, menu)
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

        Slider.init(PicassoImageLoadingService())
        banner_slider1.setAdapter(MainSliderAdapter())

        recycleView_News.layoutManager = LinearLayoutManager(context)
//        var row2 = realm?.where<tMarketUpdate>()?.findAll()
//        marketupdates.addAll(Realm.getDefaultInstance().copyFromRealm(row2))
//        recycleView_News.adapter = ListAdapter_News(marketupdates)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.opt_login -> {
                context?.startActivity(Intent(context, LoginActivity::class.java))
            }
            R.id.opt_logout -> {
                actionLogout()
            }
            R.id.opt_signup -> {
                context?.startActivity(Intent(context, RegisterActivity::class.java))
            }
            R.id.opt_chgpwd -> {
                context?.startActivity(Intent(context, ChangePwdActivity::class.java))
            }
            R.id.opt_profile -> {
                context?.startActivity(Intent(context, ProfileActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun actionLogout() {
        val builder = AlertDialog.Builder((activity as AppCompatActivity))
        builder.setTitle("Info")
        builder.setMessage("Anda ingin logout ?")
        builder.setPositiveButton("YA") { dialog, which ->
            session!!.isLogin = false
            (activity as AppCompatActivity).invalidateOptionsMenu()

            req = setRequest("auth.logout")
            URL_API.httpPost().body(req).responseJson { _, _, _ -> }

//            (activity as AppCompatActivity).navigation.menu.findItem(R.id.nav_profile_santri).isVisible = false
            context?.startActivity(Intent(context, LoginActivity::class.java))
            (activity as AppCompatActivity).finish()
        }
        builder.setNeutralButton("TIDAK") { _, _ -> }
        builder.create().show()
    }

    fun setMenuToolbar(menu: Menu?) {
        val opt_login = menu?.findItem(R.id.opt_login)
        val opt_signup = menu?.findItem(R.id.opt_signup)
        val opt_profile = menu?.findItem(R.id.opt_profile)
        val opt_chgpwd = menu?.findItem(R.id.opt_chgpwd)
        val opt_logout = menu?.findItem(R.id.opt_logout)

        opt_signup!!.isVisible = false
        if (session!!.isLogin) {
            opt_login!!.isVisible = false
            opt_profile!!.isVisible = true
            opt_chgpwd!!.isVisible = true
            opt_logout!!.isVisible = true
        } else {
            opt_login!!.isVisible = true
            opt_profile!!.isVisible = false
            opt_chgpwd!!.isVisible = false
            opt_logout!!.isVisible = false
        }

        (activity as AppCompatActivity).invalidateOptionsMenu()
    }

    class MainSliderAdapter : SliderAdapter() {
        override fun getItemCount(): Int = 10

        override fun onBindImageSlide(position: Int, viewHolder: ImageSlideViewHolder?) {
            when (position) {
                0 -> viewHolder?.bindImageSlide(R.drawable.banner_00)
                1 -> viewHolder?.bindImageSlide(R.drawable.banner_01)
                2 -> viewHolder?.bindImageSlide(R.drawable.banner_02)
                3 -> viewHolder?.bindImageSlide(R.drawable.banner_03)
                4 -> viewHolder?.bindImageSlide(R.drawable.banner_04)
                5 -> viewHolder?.bindImageSlide(R.drawable.banner_05)
                6 -> viewHolder?.bindImageSlide(R.drawable.banner_06)
                7 -> viewHolder?.bindImageSlide(R.drawable.banner_07)
                8 -> viewHolder?.bindImageSlide(R.drawable.banner_08)
                9 -> viewHolder?.bindImageSlide(R.drawable.banner_09)
            }
        }
    }

    class PicassoImageLoadingService : ImageLoadingService {
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

    class ListAdapter_News(val rows: ArrayList<tNews>) : RecyclerView.Adapter<ListAdapter_News.ListViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListViewHolder {
            return ListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_news, p0, false))
        }

        override fun getItemCount(): Int = rows.size

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(p0: ListViewHolder, p1: Int) {
            val r = rows[p1]
            p0.itemView.apply {
                txt_title.text = r.title
                txt_date.text = SimpleDateFormat(FORM_DATETIME_FORMAT).format(F().stringToDateTime(r.date))
//                Picasso.get().load(r.img_url).fit().into(img_market)
                var resID = resources.getIdentifier(
                    "mu_${(sRandom.nextInt(20) + 1)}_200",
                    "drawable",
                    context.packageName
                )
                img_news.setImageResource(resID)

//                tv_title.setOnClickListener {
                    //                    val intent = Intent(it.context, MarketUpdateActivity::class.java)
//                    it.context.startActivity(intent)
//                }
            }
        }

        class ListViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
