package com.example.DalwaApp.helper

import android.util.Patterns
import java.util.regex.Pattern

/**
 * Created by antho.firuze@gmail.com on 23/07/2019.
 */

class F {

    fun isValidPhone(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidName(name: String): Boolean {
        val regex = "^[\\p{L} .'-]+$"
        return name.matches(regex.toRegex())
    }

    fun isEmail(email: String?): Boolean {
        if (email == null || email == "") {
            return false
        }
        val regex = "\\w+(\\.|\\w)*@\\w+(\\.\\w{2,3}){1,3}"
        val pattern = Pattern.compile(regex)
        return pattern.matcher(email).matches()
    }

//    fun requiredParams(params: Array<Any>) {
//        params.forEach {
//            if (it.toString().isEmpty()) {
//                return false
//            }
//        }
//    }

}

// NOTE:
//
//(activity as AppCompatActivity)       <===      context for fragment