package com.mine.flowpay.model

import java.time.LocalDateTime

data class Transaction(
    val transactionid: Long = 0,
    val userid: Long = 0,
    val productid: Long? = null,
    val amount: Double = 0.0,
    val status: String = "",
    val paymentMethod: String = "",
    val description: String? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null
)
