package com.mine.flowpay.model

data class Users(
    val userid: Long = 0,
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val wallet: Double = 0.0,
    val phonenumber: String = "",
    val status: String = "",
    val role: String = "",
    val dateofbirth: String? = null,
    val registration: String? = null,
    val image: String? = null
)
