package com.mine.flowpay.model

data class Mail(
    val mailid: Long = 0,
    val userid: Long = 0,
    val transactionid: Long? = null,
    val subject: String = "",
    val body: String = "",
    val createdDate: String? = null,
    val isRead: Boolean = false
)
