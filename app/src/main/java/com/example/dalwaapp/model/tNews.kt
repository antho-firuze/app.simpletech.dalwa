package com.example.dalwaapp.model

import io.realm.RealmObject
import java.util.*

/**
 * Created by antho.firuze@gmail.com on 14/05/2019.
 */

open class tNews : RealmObject() {
    var id: String = UUID.randomUUID().toString()
    var title: String = ""
    var date: String = ""
    var body: String = ""
    var author: String = ""
}