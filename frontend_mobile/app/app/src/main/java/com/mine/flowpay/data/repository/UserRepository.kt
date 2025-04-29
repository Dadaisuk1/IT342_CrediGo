package com.mine.flowpay.data.repository

import com.mine.flowpay.data.Users
import com.mine.flowpay.data.dao.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: Users) = userDao.insertUser(user)
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    suspend fun getUserByUsername(username: String) = userDao.getUserByUsername(username)
    suspend fun getUserById(userId: Long) = userDao.getUserById(userId)
    suspend fun getAllUsers() = userDao.getAllUsers()
    suspend fun updateUser(user: Users) = userDao.updateUser(user)
    suspend fun deleteUser(user: Users) = userDao.deleteUser(user)
    
    // Direct wallet balance update
    suspend fun updateWalletBalance(userId: Long, newBalance: Double) = userDao.updateWalletBalance(userId, newBalance)
} 