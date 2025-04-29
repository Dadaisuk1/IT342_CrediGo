package com.mine.flowpay.app

import android.app.Application
import android.content.SharedPreferences
import com.mine.flowpay.data.Users
import com.mine.flowpay.network.RetrofitInstance
import com.mine.flowpay.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class FlowpayApp : Application() {
    lateinit var sessionManager: SessionManager
    private lateinit var prefs: SharedPreferences

    var loggedInuser: Users? = null
        set(value) {
            field = value
            if (value != null) {
                sessionManager.saveLoginState(value.user_id)
            } else {
                sessionManager.clearLoginState()
            }
        }

    var isLoggedIn: Boolean = false
        get() = loggedInuser != null

    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager(this)
        prefs = getSharedPreferences("flowpay_prefs", MODE_PRIVATE)

        // Retrofit-based initialization (no local DB)
        // Optionally, fetch categories, products, and user info from backend here if needed

        // Restore login state from sessionManager and backend
        val userId = sessionManager.getUserId()
        if (userId != -1L && sessionManager.isLoggedIn()) {
            // Fetch user info from backend using Retrofit
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val response = RetrofitInstance.api.getUserById(userId)
                    if (response.isSuccessful) {
                        loggedInuser = response.body()
                    } else {
                        loggedInuser = null
                    }
                } catch (e: Exception) {
                    loggedInuser = null
                }
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        // No DB callbacks to remove
    }
}
