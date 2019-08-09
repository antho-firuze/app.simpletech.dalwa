package com.example.dalwaapp.model

import java.util.*

data class tTransaction(
    val account_no: String,
    val admin_charge: Double,
    val bank_code: String,
    val created_at: String,
    val grand_total: Double,
    val partner_id: Int,
    val payed_at: String,
    val payment_id: Int,
    val payment_method_id: Int,
    val payment_no: String,
    val payment_status_desc: String,
    val payment_status_id: Int,
    val sub_total: Double
)