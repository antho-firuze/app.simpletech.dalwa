package com.example.DalwaApp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.example.DalwaApp.helper.Session
import com.example.DalwaApp.model.*
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Currency & Percent Format
 */
const val ANSI_CURRENCY_FORMAT = "#,##0.00;-#,##0.00"
const val IDN_CURRENCY_FORMAT = "#.##0,00;-#.##0,00"
const val ANSI_PERCENT_FORMAT = "#,##0.00 %;-#,##0.00 %"
const val IDN_PERCENT_FORMAT = "#.##0,00 %;-#.##0,00 %"
val CurrFmt = DecimalFormat(ANSI_CURRENCY_FORMAT)
val PercentFmt = DecimalFormat(ANSI_PERCENT_FORMAT)

/**
 * Date Format
 */
const val ANSI_DATE_FORMAT = "yyyy-MM-dd"
const val EUROPE_DATE_FORMAT = "dd.MM.yyyy"
const val IDN_DATE_FORMAT = "dd-MM-yyyy"
const val FORM_DATE_FORMAT = "dd-MMM-yyyy"

const val ANSI_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"  // 2018-01-15 21:35:00
const val ANSI_DATETIME_FORMAT_WITH_MS = "yyyy-MM-dd HH:mm:ss.SSS"  // 2015-01-01 00:00:00.000
const val IDN_DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss"
const val FORM_DATETIME_FORMAT = "dd-MMM-yyyy HH:mm:ss"
const val JAVA_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

//val DateFmt = SimpleDateFormat(IDN_DATE_FORMAT)
//val DateTimeFmt = SimpleDateFormat(IDN_DATETIME_FORMAT)
//val FormDateFmt = SimpleDateFormat(FORM_DATE_FORMAT)
//val FormDateTimeFmt = SimpleDateFormat(FORM_DATETIME_FORMAT)
//var JavaDateTimeFmt = SimpleDateFormat(JAVA_DATETIME_FORMAT)
//var AnsiDateTimeFmt = SimpleDateFormat(ANSI_DATETIME_FORMAT)
var isLogin = false
var session: Session? = null

val jRequest = JSONObject()
//val jParams = JSONObject()
val sRandom = Random()
var aMain = Activity()

//val URL_API = "https://api.simpipro.com"
//val AGENT = "android"
//val APPCODE = "ayoavram.webapp"

val URL_API = "http://localhost:5050"
val AGENT = "android"
val APPCODE = "dalwa.app"

var realm: Realm? = null
var req: String = ""

var pubVar: Map<Any, Any> = emptyMap()

//val NAVTimeStamp = SimpleDateFormat("yyyy-MM-dd 11:00:00").format(Date())
val NAVTimeStamp = SimpleDateFormat("yyyy-MM-dd 11:00:00").format(Date())

fun setRequest(method: String, params: Map<Any, Any> = emptyMap()): String {
    jRequest.put("method", method)
    if (params.isNotEmpty())
        jRequest.put("params", JSONObject(params))

    return jRequest.toString()
}

fun stringToDateTime(dt: String, fmt: String = ANSI_DATETIME_FORMAT): Date {
    var DateTimeFmt = SimpleDateFormat(fmt)
    return DateTimeFmt.parse(dt)
}

private fun diffOfDaysByDateAndTime( a: Calendar, b: Calendar ): Int {

    val duration = a.timeInMillis - b.timeInMillis
    return TimeUnit.MILLISECONDS.toDays(duration).toInt()
}

private fun diffOfDaysByDate( a: Calendar, b: Calendar ): Int {

    clearTimeComponent(a)
    clearTimeComponent(b)

    return diffOfDaysByDateAndTime(a, b)
}

private fun clearTimeComponent( date: Calendar ) {

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

class MainApplication : Application() {

//    companion object {
//        var session: Session? = null
//    }

    override fun onCreate() {
        super.onCreate()

        session = Session(applicationContext)

        jRequest.put("id", sRandom.nextInt()).put("appcode", APPCODE).put("agent", AGENT)

        // ... Initialization Realm Database
        Realm.init(this)
        val config =
            RealmConfiguration.Builder().name("simpleFund.realm").schemaVersion(1).deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

        if (isDownloadPortfolioDashboard())
            getPortfolioDashboard()
        getPortfolioMaster()
        getMarketUpdate()
    }

    @SuppressLint("NewApi")
    private fun isDownloadPortfolioDashboard(): Boolean {
        val current = LocalDateTime.now()
        var rows = realm?.where<tPorfolioDashboard>()?.findAll()
        if (rows != null) {
            if (arrayListOf("SATURDAY", "SUNDAY").contains(current.dayOfWeek.toString())) {

                return false
            } else {

                var row: tTimestamp? =
                    realm?.where<tTimestamp>()?.equalTo("field", "PortfolioDashboard")?.findFirst() ?: return true

                if (current.hour.toInt() < 10) {

                    return row?.timestamp?.day != (current.dayOfMonth - 2)
                } else {

                    return row?.timestamp?.day != (current.dayOfMonth - 1)
                }
            }
        }

        return true
    }

    fun getPortfolioDashboard() {
//        var row = realm?.where<tTimestamp>()?.equalTo("field", "PortfolioDashboard")?.findFirst()
//        if (row.timestamp



//        req = setRequest("simple_fund.reksadana_beranda", mapOf("limit" to 2))
        req = setRequest("simple_fund.reksadana_beranda")
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.size > 0) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {

                    tPortfolioDashboardManager().deleteAll()
                    tPortfolioDashboardManager().insertFromJsonList(
                        (dataRes.obj()["result"] as JSONObject).getJSONArray(
                            "rows"
                        )
                    )
                    insertFieldTimeStamp("PortfolioDashboard", NAVTimeStamp)

//                    snackbar(main_layout, "Loading data berhasil")
                } else {
//                    snackbar(main_layout, "Error Load Data: "+dataRes.obj().getString("message"))
                }
            } else {
//                snackbar(main_layout,"Error (Balance): Ajax request failed")
            }
        }
    }

    private fun insertFieldTimeStamp(field: String, timeStamp: String?) {
        realm?.executeTransaction {
            val obj = realm!!.createObject(tTimestamp::class.java)
            obj.field = field
            obj.timestamp = stringToDateTime(timeStamp!!)
        }
    }

    fun getPortfolioMaster() {
        req = setRequest("simple_fund.reksadana_master")
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.size > 0) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    tPortfolioManager().insertFromJsonList((dataRes.obj()["result"] as JSONObject).getJSONArray("rows"))
//                    snackbar(main_layout, "Loading data berhasil (master)")
                } else {
//                    snackbar(main_layout, "Error Load Data: "+dataRes.obj().getString("message"))
                }
            } else {
//                snackbar(main_layout,"Error (Balance): Ajax request failed")
            }
        }
    }

    fun getMarketUpdate() {
        req = setRequest("simple_fund.market_update")
        URL_API.httpPost().body(req).responseJson { _, resp, res ->
            if (resp.data.size > 0) {
                val (dataRes, _) = res
                if (dataRes!!.obj().getBoolean("status")) {
                    tMarketUpdateManager().insertFromJsonList((dataRes.obj()["result"] as JSONObject).getJSONArray("rows"))
//                    snackbar(main_layout, "Loading data berhasil (market_update)")
                } else {
//                    snackbar(main_layout, "Error Load Data: "+dataRes.obj().getString("message"))
                }
            } else {
//                snackbar(main_layout,"Error (Balance): Ajax request failed")
            }
        }
    }


}