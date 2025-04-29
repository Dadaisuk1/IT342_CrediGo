package com.mine.flowpay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mine.flowpay.data.Users
import com.mine.flowpay.data.database.AppDatabase
import com.mine.flowpay.data.repository.UserRepository
import com.mine.flowpay.app.FlowpayApp
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    private val _currentUser = MutableLiveData<Users>()
    val currentUser: LiveData<Users> = _currentUser
    private val _allUsers = MutableLiveData<List<Users>>()
    val allUsers: LiveData<List<Users>> = _allUsers
    private val _walletUpdateResult = MutableLiveData<Boolean>()
    val walletUpdateResult: LiveData<Boolean> = _walletUpdateResult

    init {
        val database = (application as FlowpayApp).database
        repository = UserRepository(database.userDao())
        refreshUsers()
    }

    private fun refreshUsers() {
        viewModelScope.launch {
            _allUsers.value = repository.getAllUsers()
        }
    }

    private fun refreshCurrentUser(userId: Long) {
        viewModelScope.launch {
            repository.getUserById(userId)?.let {
                _currentUser.value = it
            }
        }
    }

    fun insertUser(user: Users) = viewModelScope.launch {
        repository.insertUser(user)
        refreshUsers()
    }

    fun updateUser(user: Users) = viewModelScope.launch {
        repository.updateUser(user)
        refreshUsers()
        // If this is the current user, update current user as well
        if (user.user_id == _currentUser.value?.user_id) {
            refreshCurrentUser(user.user_id)
        }
    }

    fun deleteUser(user: Users) = viewModelScope.launch {
        repository.deleteUser(user)
        refreshUsers()
    }

    fun getUserById(userId: Long) {
        viewModelScope.launch {
            repository.getUserById(userId)?.let {
                _currentUser.value = it
            }
        }
    }

    fun getUserByEmail(email: String) = viewModelScope.launch {
        repository.getUserByEmail(email)?.let {
            _currentUser.value = it
        }
    }

    fun getUserByUsername(username: String) = viewModelScope.launch {
        repository.getUserByUsername(username)?.let {
            _currentUser.value = it
        }
    }

    // Update wallet balance for a user
    fun updateWalletBalance(userId: Long, newBalance: Double) = viewModelScope.launch {
        try {
            val success = repository.safeUpdateWalletBalance(userId, newBalance)
            _walletUpdateResult.value = success
            
            if (success) {
                // Refresh both current user and all users
                refreshCurrentUser(userId)
                refreshUsers()
            }
        } catch (e: Exception) {
            _walletUpdateResult.value = false
            e.printStackTrace()
        }
    }

    // Get current wallet balance
    fun getWalletBalance(userId: Long) = viewModelScope.launch {
        try {
            repository.getWalletBalance(userId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
} 