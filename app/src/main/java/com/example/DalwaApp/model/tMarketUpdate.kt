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

open class tMarketUpdate : RealmObject() {
    var id: String = UUID.randomUUID().toString()
    var ReviewID: Int = 0
    var ReviewTitle: String = ""
    var ReviewDate: String = ""
    var ReviewBody: String = ""
    var ReviewAuthor: String = ""
}

class tMarketUpdateManager {
    var realm: Realm = Realm.getDefaultInstance()

    fun find(): tMarketUpdate? = realm.where<tMarketUpdate>().findFirst()

    fun findAll(): List<tMarketUpdate> = realm.where<tMarketUpdate>().findAll()!!

    fun insertFromJson(j: JSONObject) =
        realm.executeTransaction { realm -> realm.createObjectFromJson(tMarketUpdate::class.java, j) }

    fun insertFromJsonList(j: JSONArray) {
        tMarketUpdateManager().deleteAll()

        try {
            // Open a transaction to store items into the realm
            realm.beginTransaction()
            realm.createAllFromJson(tMarketUpdate::class.java, j)
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
            realm.delete<tMarketUpdate>()
            realm.commitTransaction()
        } finally {
            realm.close()
        }
    }

}