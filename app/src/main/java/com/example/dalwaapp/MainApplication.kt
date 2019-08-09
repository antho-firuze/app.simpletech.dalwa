package com.example.dalwaapp

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.example.dalwaapp.helper.Session
import io.realm.Realm
import io.realm.RealmConfiguration
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.indeterminateProgressDialog
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Currency & Percent Format
 */
//const val ANSI_CURRENCY_FORMAT = "#,##0.00;-#,##0.00"
//const val ANSI_PERCENT_FORMAT = "#,##0.00 %;-#,##0.00 %"
//val currFmtEN = DecimalFormat(ANSI_CURRENCY_FORMAT)
//val percentFmt = DecimalFormat(ANSI_PERCENT_FORMAT)

val localeID = Locale("in", "ID")
val currFmtID = NumberFormat.getCurrencyInstance(localeID)


/**
 * Date Format
 */
const val ANSI_DATE_FORMAT = "yyyy-MM-dd"
const val EUROPE_DATE_FORMAT = "dd.MM.yyyy"
const val IDN_DATE_FORMAT = "dd-MM-yyyy"
const val FORM_DATE_FORMAT = "dd-MMM-yyyy"

const val ANSI_0_DATETIME_FORMAT = "yyyy-MM-dd HH:mm"  // 2018-01-15 21:35
const val ANSI_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"  // 2018-01-15 21:35:00
const val ANSI_DATETIME_FORMAT_WITH_MS = "yyyy-MM-dd HH:mm:ss.SSS"  // 2015-01-01 00:00:00.000
const val IDN_DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss"
const val FORM_0_DATETIME_FORMAT = "dd-MMM-yyyy HH:mm"
const val FORM_DATETIME_FORMAT = "dd-MMM-yyyy HH:mm:ss"
const val JAVA_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

//val DateFmt = SimpleDateFormat(IDN_DATE_FORMAT)
//val DateTimeFmt = SimpleDateFormat(IDN_DATETIME_FORMAT)
//val FormDateFmt = SimpleDateFormat(FORM_DATE_FORMAT)
//val FormDateTimeFmt = SimpleDateFormat(FORM_DATETIME_FORMAT)
//var JavaDateTimeFmt = SimpleDateFormat(JAVA_DATETIME_FORMAT)
//var AnsiDateTimeFmt = SimpleDateFormat(ANSI_DATETIME_FORMAT)

//val URL_API = "http://192.168.43.72:5050"
//val URL_API = "http://192.168.1.33:5050"
val URL_API = "https://api.kenziotech.com"
val AGENT = "android"
val APPCODE = "dalwa.app"
val LANG = "id"

var isRefresh = false
var session: Session? = null

val jRequest = JSONObject()
val sRandom = Random()

var realm: Realm? = null
var req: String = ""

var pubVar: MutableMap<Any, Any> = mutableMapOf()

//val NAVTimeStamp = SimpleDateFormat("yyyy-MM-dd 11:00:00").format(Date())
val DateTimeStamp = SimpleDateFormat("yyyy-MM-dd 11:00:00").format(Date())

fun setRequest(method: String, params: Map<Any, Any> = emptyMap()): String {
    jRequest.put("method", method)
    if (params.isNotEmpty())
        jRequest.put("params", JSONObject(params))

    if (session!!.token.isNotEmpty())
        jRequest.put("token", session!!.token)

    return jRequest.toString()
}

class MainApplication : Application() {

    companion object {
//        var isRefresh = false
//        var session: Session? = null
    }

    override fun onCreate() {
        super.onCreate()

        session = Session(applicationContext)

        jRequest
            .put("id", sRandom.nextInt())
            .put("appcode", APPCODE)
            .put("agent", AGENT)
            .put("lang", LANG)

        // ... Initialization Realm Database
        Realm.init(this)
        val config =
            RealmConfiguration.Builder().name("dalwaApp.realm").schemaVersion(1).deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()

    }

}