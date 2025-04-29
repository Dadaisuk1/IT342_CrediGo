package com.mine.flowpay.data.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import com.mine.flowpay.data.Users

@Dao
interface UserDao {
    // Create
    @Insert
    suspend fun insertUser(user: Users)

    //Get user by ID
    @Query("SELECT * FROM users WHERE user_id = :userId")
    suspend fun getUserById(userId: Long): Users?

    //Get user by email
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): Users?

    //Get user by username
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): Users?

    // Get all users
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<Users>

    //Update
    @Update
    suspend fun updateUser(user: Users)

    //Delete
    @Delete
    suspend fun deleteUser(user: Users)

    // Update wallet balance with transaction
    @Transaction
    @Query("UPDATE users SET wallet_balance = :newBalance WHERE user_id = :userId")
    suspend fun updateWalletBalance(userId: Long, newBalance: Double)

    // Get user's wallet balance
    @Query("SELECT wallet_balance FROM users WHERE user_id = :userId")
    suspend fun getWalletBalance(userId: Long): Double?
} 