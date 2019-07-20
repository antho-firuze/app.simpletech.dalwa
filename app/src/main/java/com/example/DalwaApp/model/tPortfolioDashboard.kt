package com.example.DalwaApp.model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.delete
import io.realm.kotlin.where
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

open class tPorfolioDashboard : RealmObject() {
    var PortfolioID: Int = 0
    var PortfolioNameShort: String = ""
    var Ccy: String = ""
    var PositionDate: Date? = null
    var NAVperUnit: Double = 0.0
    var rYTD: Double = 0.0
}

class tPortfolioDashboardManager {
    var realm: Realm = Realm.getDefaultInstance()

    fun find(): tPorfolioDashboard? = realm.where<tPorfolioDashboard>().findFirst()

    fun findAll(): List<tPorfolioDashboard?> = realm.where<tPorfolioDashboard>().findAll()

    fun insertFromJson(j: JSONObject) =
        realm.executeTransaction { realm -> realm.createObjectFromJson(tPorfolioDashboard::class.java, j) }

    fun insertFromJsonList(j: JSONArray) {
        tPortfolioDashboardManager().deleteAll()

        try {
            // Open a transaction to store items into the realm
            realm.beginTransaction()
            realm.createAllFromJson(tPorfolioDashboard::class.java, j)
            realm.commitTransaction()
        } catch (e: IOException) {
            // Remember to cancel the transaction if anything goes wrong.
            if (realm.isInTransaction) {
                realm.cancelTransaction()
            }
            throw RuntimeException(e)
        } finally {
            realm.close()
        }
    }

    fun deleteAll() {
        try {
            realm.beginTransaction()
            realm.delete<tPorfolioDashboard>()
            realm.commitTransaction()
        } finally {
            realm.close()
        }
    }

}