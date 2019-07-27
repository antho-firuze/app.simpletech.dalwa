package com.example.dalwaapp.model

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

open class tUser : RealmObject() {
    var id: String = UUID.randomUUID().toString()
    var ClientID: Int = 0
    var email: String = ""
    var full_name: String = ""
    var token: String = ""
    var token_exp: Date? = null
    var token_exp_epoch: Long = 0
}

class tUserManager{
    var realm: Realm = Realm.getDefaultInstance()

    fun find(): tUser? = realm.where<tUser>().findFirst()

    fun findAll(): List<tUser> = realm.where<tUser>().findAll()

    fun findById(id: Long): tUser? = realm.where<tUser>().equalTo("id", id).findFirst()

    fun insertFromJson(j: JSONObject) =
        realm.executeTransaction { realm -> realm.createObjectFromJson(tUser::class.java, j) }

    fun insertFromJsonList(j: JSONArray) {
        try {
            // Open a transaction to store items into the realm
            realm.beginTransaction()
            realm.createAllFromJson(tUser::class.java, j)
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
                val results = realm.where(tUser::class.java).findAll().deleteAllFromRealm()
            }
        } finally {
            realm.close()
        }
    }
}