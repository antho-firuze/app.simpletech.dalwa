package com.example.DalwaApp.helper

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager

/**
 * Created by antho.firuze@gmail.com on 17/07/2019.
 */

class Session(ctx: Context) {

    val PREF_NAME = "com.example.DalwaApp"
    val pref: SharedPreferences = ctx.getSharedPreferences(PREF_NAME, 0)


    var bgColor: Int
        get() = pref.getInt("bg_color", Color.BLACK)
        set(value) = pref.edit().putInt("bg_color", value).apply()

    var isLogin: Boolean
        get() = pref.getBoolean("isLogin", false)
        set(value) = pref.edit().putBoolean("isLogin", value).apply()
}