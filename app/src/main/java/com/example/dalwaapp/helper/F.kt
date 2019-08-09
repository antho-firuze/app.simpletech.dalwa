package com.example.dalwaapp.helper

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import com.example.dalwaapp.*
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Created by antho.firuze@gmail.com on 23/07/2019.
 */

class F {

    fun isValidPhone(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidName(name: String): Boolean {
        val regex = "^[\\p{L} .'-]+$"
        return name.matches(regex.toRegex())
    }

    fun isEmail(email: String?): Boolean {
        if (email == null || email == "") {
            return false
        }
        val regex = "\\w+(\\.|\\w)*@\\w+(\\.\\w{2,3}){1,3}"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(email).matches()
    }

    fun stringToDateTime(dt: String, fmt: String = ANSI_DATETIME_FORMAT): Date {
        var DateTimeFmt = SimpleDateFormat(fmt)
        return DateTimeFmt.parse(dt)
    }

    private fun diffOfDaysByDateAndTime(a: Calendar, b: Calendar): Int {

        val duration = a.timeInMillis - b.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(duration).toInt()
    }

    private fun diffOfDaysByDate(a: Calendar, b: Calendar): Int {

        clearTimeComponent(a)
        clearTimeComponent(b)

        return diffOfDaysByDateAndTime(a, b)
    }

    private fun clearTimeComponent(date: Calendar) {

        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        date.set(Calendar.MILLISECOND, 0)
    }

    class Time(internal var hours: Int, internal var minutes: Int, internal var seconds: Int)

    fun diffTime(start: Time, stop: Time): Time {
        val diff = Time(0, 0, 0)
        if (stop.seconds > start.seconds) {
            --start.minutes
            start.seconds += 60
        }
        diff.seconds = start.seconds - stop.seconds
        if (stop.minutes > start.minutes) {
            --start.hours
            start.minutes += 60
        }
        diff.minutes = start.minutes - stop.minutes
        diff.hours = start.hours - stop.hours
        return diff
    }

    fun actionLogout(ctx: Context?) {
        session!!.isLogin = false
        session!!.username = ""
        session!!.token = ""

        ctx?.startActivity(Intent(ctx, Main2Activity::class.java))
        (ctx as AppCompatActivity).finish()

        req = setRequest("auth.logout")
        URL_API.httpPost().body(req).responseJson { _, _, _ -> }
    }

//    fun requiredParams(params: Array<Any>) {
//        params.forEach {
//            if (it.toString().isEmpty()) {
//                return false
//            }
//        }
//    }

}

//lbl_created_date.text = "${SimpleDateFormat(FORM_0_DATETIME_FORMAT).format(F().stringToDateTime(r.created_at))} WIB"
//val DateTimeStamp = SimpleDateFormat("yyyy-MM-dd 11:00:00").format(Date())

// NOTE: (ACTIVITY)
//=================
//builder.setNeutralButton("OK") { dialog, which ->
//    val needLogin = dataRes.obj().getString("need_login")
//    if (needLogin.isNotEmpty()) {
//        F().actionLogout(this)
//    }
//}

// NOTE: (FRAGMENT)
//=================
//(activity as AppCompatActivity)       <===      context for fragment

//var selectedRows2: MutableMap<Int, Number> = mutableMapOf()
//selectedRows2.put(k, value)
//selectedRows2.remove(k)
//val ttlAmount2 = selectedRows2.map { it.value.toDouble() }.sum()

//builder.setNeutralButton("OK") { dialog, which ->
//    val needLogin = dataRes.obj().getString("need_login")
//    if (needLogin.isNotEmpty()) {
//        F().actionLogout((activity as AppCompatActivity))
//    }
//}
