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

open class tPorfolio : RealmObject() {
    var PortfolioID: Int = 0
    var PortfolioCode: String = ""
    var PortfolioNameShort: String = ""
    var Ccy: String = ""
    var AssetTypeCode: String = ""
    var AssetTypeDescription: String = ""
    var IsSyariah: String = ""
    var RiskTolerance: String = ""
    var RiskLevel: Int = 0
    var RiskScore: Int = 0
    var InceptionDate: Date? = null
    var CustodianBank: String = ""
    var StatusCode: String = ""
    var InvestmentObjective: String = ""
    var MinimumInitialSubscription: String = ""
    var MinimumAdditionalSubscription: String = ""
    var MinimumRedemption: String = ""
    var FeeCustodian: String = ""
    var FeeSelling: String = ""
    var FeeRedemption: String = ""
    var FeeSwicthing: String = ""
    var FeeManagement: String = ""
    var apply_subscription: String = ""
    var apply_redemption: String = ""
    var apply_switching: String = ""
    var apply_booking: String = ""
    var start_booking: Date? = null
    var MinimumSwitching: String = ""
}

class tPortfolioManager {
    var realm: Realm = Realm.getDefaultInstance()

    fun find(): tPorfolio? = realm.where<tPorfolio>().findFirst()

    fun findAll(): List<tPorfolio> = realm.where<tPorfolio>().findAll()

    fun insertFromJson(j: JSONObject) =
        realm.executeTransaction { realm -> realm.createObjectFromJson(tPorfolio::class.java, j) }

    fun insertFromJsonList(j: JSONArray) {
        try {
            // Open a transaction to store items into the realm
            realm.beginTransaction()
            realm.createAllFromJson(tPorfolio::class.java, j)
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
            realm.delete<tPorfolio>()
            realm.commitTransaction()
        } finally {
            realm.close()
        }
//        try {
//            realm.executeTransaction { realm ->
//                val results = realm.where(tPorfolio::class.java).findAll().deleteAllFromRealm()
//            }
//        } finally {
//            realm?.close()
//        }
    }

}