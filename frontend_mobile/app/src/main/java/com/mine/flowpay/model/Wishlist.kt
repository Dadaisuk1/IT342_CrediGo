package com.mine.flowpay.model

data class Wishlist(
    val wishlistid: Long = 0,
    val userid: Long = 0,
    val productid: Long = 0,
    val addedAt: String? = null
)
