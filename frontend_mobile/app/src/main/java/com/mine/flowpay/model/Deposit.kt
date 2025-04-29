package com.mine.flowpay.model

data class Deposit(
    val depositid: Long = 0,
    val userid: Long = 0,
    val amount: Double = 0.0,
    val datetime: String? = null,
    val paymentOption: String = "" // "gcash", "maya", "visa", "mastercard"
)
