package com.example.DalwaApp.model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.where
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

open class tTimestamp : RealmObject() {
//    var id: String = UUID.randomUUID().toString()
    var field: String = ""
    var timestamp: Date? = null
}

class tTimestampManager {
    var realm: Realm = Realm.getDefaultInstance()

    fun find(): tTimestamp? = realm.where<tTimestamp>().findFirst()

    fun findAll(): List<tTimestamp> = realm.where<tTimestamp>().findAll()

    fun insertFromJson(j: JSONObject) =
        realm.executeTransaction { realm -> realm.createObjectFromJson(tTimestamp::class.java, j) }

    fun insertFromJsonList(j: JSONArray) {
        try {
            // Open a transaction to store items into the realm
            realm.beginTransaction()
            realm.createAllFromJson(tTimestamp::class.java, j)
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
            realm.executeTransaction { realm ->
                val results = realm.where(tTimestamp::class.java).findAll().deleteAllFromRealm()
            }
        } finally {
            realm?.close()
        }
    }

}