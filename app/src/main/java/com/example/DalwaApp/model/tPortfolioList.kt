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

open class tPorfolioList : RealmObject() {
    var PortfolioID: Int = 0
    var PortfolioNameShort: String = ""
    var Ccy: String = ""
    var AssetTypeCode: String = ""
    var AssetTypeDescription: String = ""
    var IsSyariah: String = ""
    var RiskTolerance: String = ""
    var RiskLevel: Int = 0
    var RiskScore: Int = 0
    var PositionDate: Date? = null
    var NAVperUnit: Double = 0.00
    var rYTD: Double = 0.00
}

class tPortfolioListManager {
    var realm: Realm = Realm.getDefaultInstance()

    fun find(): tPorfolioList? = realm.where<tPorfolioList>().findFirst()

    fun findAll(): List<tPorfolioList> = realm.where<tPorfolioList>().findAll()

    fun insertFromJson(j: JSONObject) =
        realm.executeTransaction { realm -> realm.createObjectFromJson(tPorfolioList::class.java, j) }

    fun insertFromJsonList(j: JSONArray) {
        try {
            // Open a transaction to store items into the realm
            realm.beginTransaction()
            realm.createAllFromJson(tPorfolioList::class.java, j)
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
            realm.delete<tPorfolioList>()
            realm.commitTransaction()
        } finally {
            realm.close()
        }
//        try {
//            realm.executeTransaction { realm ->
//                val results = realm.where(tPorfolioList::class.java).findAll().deleteAllFromRealm()
//            }
//        } finally {
//            realm?.close()
//        }
    }

}