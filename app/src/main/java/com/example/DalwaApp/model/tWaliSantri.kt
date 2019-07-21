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

open class tWaliSantri : RealmObject() {
    var id: String = UUID.randomUUID().toString()
    var partner_id: Int = 0
    var first_name: String = ""
    var last_name: String = ""
    var email: String = ""
    var phone: String = ""
}