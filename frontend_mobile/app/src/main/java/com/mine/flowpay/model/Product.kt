package com.mine.flowpay.model

data class Product(
    val productid: Long = 0,
    val productname: String = "",
    val price: Double = 0.0,
    val image: String? = null,
    val category: ProductCategory? = null
)
