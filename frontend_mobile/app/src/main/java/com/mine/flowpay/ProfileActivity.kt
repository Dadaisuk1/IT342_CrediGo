package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mine.flowpay.model.Users
import com.mine.flowpay.network.RetrofitInstance
import com.mine.flowpay.utils.DialogUtils

class ProfileActivity : AppCompatActivity() {
    private lateinit var currentUser: Users

    // UI components
    private lateinit var usernameTextView: TextView
    private lateinit var balanceTextView: TextView
    private lateinit var emailTextView: TextView

    // Menu items
    private lateinit var walletMenu: LinearLayout
    private lateinit var mailsMenu: LinearLayout
    private lateinit var transactionsMenu: LinearLayout
    private lateinit var wishlistMenu: LinearLayout
    private lateinit var searchMenu: LinearLayout
    private lateinit var settingsMenu: LinearLayout
    private lateinit var logoutMenu: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Get current user from application
        val app = application as FlowpayApp
        currentUser = app.loggedInuser ?: run {
            // If no user is logged in, redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Optionally, refresh user info from backend
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getUserById(currentUser.user_id)
            if (response.isSuccessful) {
                response.body()?.let {
                    currentUser = it
                    (application as FlowpayApp).loggedInuser = it
                    updateUI()
                }
            }
        }

        // Initialize UI components
        initializeViews()

        // Set up UI with user data
        updateUI()

        // Set up click listeners
        setupClickListeners()
    }

    private fun initializeViews() {
        // Find views by ID
        usernameTextView = findViewById(R.id.text_username)
        balanceTextView = findViewById(R.id.text_balance)
        emailTextView = findViewById(R.id.tv_email)

        // Menu items
        walletMenu = findViewById(R.id.menu_wallet)
        mailsMenu = findViewById(R.id.menu_mails)
        transactionsMenu = findViewById(R.id.menu_transactions)
        wishlistMenu = findViewById(R.id.menu_wishlist)
        searchMenu = findViewById(R.id.menu_search)
        settingsMenu = findViewById(R.id.menu_settings)
        logoutMenu = findViewById(R.id.menu_logout)
    }

    private fun updateUI() {
        // Set username and balance
        usernameTextView.text = currentUser.username
        // Format wallet balance with commas and two decimal points
        val formattedBalance = String.format("%,.2f", currentUser.walletBalance)
        balanceTextView.text = "₱$formattedBalance"
        // Set user email
        emailTextView.text = currentUser.email
    }

    private fun setupClickListeners() {
        // Wallet menu click
        walletMenu.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        // Menu item clicks
        mailsMenu.setOnClickListener {
            startActivity(Intent(this, MailsActivity::class.java))
        }

        transactionsMenu.setOnClickListener {
            startActivity(Intent(this, TransactionActivity::class.java))
        }

        wishlistMenu.setOnClickListener {
            startActivity(Intent(this, LikesActivity::class.java))
        }

        searchMenu.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        settingsMenu.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        logoutMenu.setOnClickListener {
            // Show logout confirmation dialog
            DialogUtils.showCustomConfirmationDialog(
                this,
                "Logout",
                "Are you sure you want to logout?",
                {
                    // Log out the user
                    (application as FlowpayApp).loggedInuser = null

                    // Navigate to login screen
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            )
        }
    }
}
