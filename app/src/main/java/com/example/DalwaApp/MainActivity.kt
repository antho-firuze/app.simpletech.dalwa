package com.example.DalwaApp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.DalwaApp.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val fragmentHome: Fragment = HomeFragment()
    val fragmentWali: Fragment = WaliFragment()
    val fragmentSantri: Fragment = SantriFragment()
    val fragmentFaq: Fragment = FaqFragment()
    var activeFragment: Fragment = fragmentHome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragmentWali, "2").hide(fragmentWali).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSantri, "3").hide(fragmentSantri).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragmentFaq, "4").hide(fragmentFaq).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragmentHome, "1").commit()

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(fragmentHome).commit()
                    activeFragment = fragmentHome
                }
                R.id.nav_profile_wali  -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(fragmentWali).commit()
                    activeFragment = fragmentWali
                }
                R.id.nav_profile_santri -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(fragmentSantri).commit()
                    activeFragment = fragmentSantri
                }
                R.id.nav_faq -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(fragmentFaq).commit()
                    activeFragment = fragmentFaq
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        navigationMenuRefresh()

    }

    override fun onResume() {
        super.onResume()
        navigationMenuRefresh()
    }

    fun navigationMenuRefresh() {
        var menu = navigation.menu
        menu.findItem(R.id.nav_profile_wali).isVisible = false
        menu.findItem(R.id.nav_profile_santri).isVisible = session!!.isLogin
    }

}
