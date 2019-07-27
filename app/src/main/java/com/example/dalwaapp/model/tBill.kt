package com.example.dalwaapp.model

import java.util.*

data class tBill(
    val amount: Double,
    val bill_id: Int,
    val bill_status: String,
    val bill_status_id: Int,
    val desc: String,
    val due_date: Date,
    val partner_id: Int
)