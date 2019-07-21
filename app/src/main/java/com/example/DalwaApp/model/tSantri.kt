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

open class tSantri : RealmObject() {
    var id: String = UUID.randomUUID().toString()
    var partner_id: Int = 0
    var reg_no: String = ""
    var first_name: String = ""
    var last_name: String = ""
    var region: String = ""
    var class_diniyah: String = ""
    var class_umum: String = ""
    var room: String = ""
    var sex: String = ""
    var father_name: String = ""
    var father_activity: String = ""
    var mother_name: String = ""
    var mother_activity: String = ""
}