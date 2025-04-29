package com.mine.flowpay.network

import com.mine.flowpay.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Users
    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") userId: Long): Response<Users>

    @POST("/api/users/register")
    suspend fun registerUser(@Body user: Users): Response<Users>

    // Products
    @GET("/api/products/getActiveProducts")
    suspend fun getActiveProducts(): Response<List<Product>>

    // Transactions
    @GET("/api/transactions/user/{userId}")
    suspend fun getUserTransactions(@Path("userId") userId: Long): Response<List<Transaction>>

    @POST("/api/transactions/create")
    suspend fun createTransaction(@Body transaction: Transaction): Response<Transaction>

    // Deposits
    @GET("/api/deposits/user/{userId}")
    suspend fun getUserDeposits(@Path("userId") userId: Long): Response<List<Deposit>>

    @POST("/api/deposits/create")
    suspend fun createDeposit(@Body deposit: Deposit): Response<Deposit>

    // Wishlist
    @GET("/api/wishlist/user/{userId}")
    suspend fun getUserWishlist(@Path("userId") userId: Long): Response<List<Wishlist>>

    @POST("/api/wishlist/add")
    suspend fun addToWishlist(@Body wishlist: Wishlist): Response<Wishlist>

    // Mail
    @GET("/api/mails/getUserMails/{userId}")
    suspend fun getUserMails(@Path("userId") userId: Long): Response<List<Mail>>
}
