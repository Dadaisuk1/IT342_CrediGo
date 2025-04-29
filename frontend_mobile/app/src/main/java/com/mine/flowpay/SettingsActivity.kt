package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.data.Users
import com.mine.flowpay.utils.DialogUtils

class SettingsActivity : AppCompatActivity() {
    private lateinit var currentUser: Users

    // Menu items
    private lateinit var editProfileMenu: LinearLayout
    private lateinit var aboutUsMenu: LinearLayout
    private lateinit var contactUsMenu: LinearLayout
    private lateinit var developersMenu: LinearLayout
    private lateinit var logoutMenu: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Get current user from application
        val app = application as FlowpayApp
        currentUser = app.loggedInuser ?: run {
            // If no user is logged in, redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Initialize UI components
        initializeViews()

        // Set up click listeners
        setupClickListeners()

        // Set header title
        val headerTitle = findViewById<TextView>(R.id.tv_header_title)
        headerTitle.text = "Settings"
        findViewById<ImageView>(R.id.iv_back).setOnClickListener { onBackPressed() }
    }

    private fun initializeViews() {
        // Menu items
        editProfileMenu = findViewById(R.id.menu_edit_profile)
        aboutUsMenu = findViewById(R.id.menu_about_us)
        contactUsMenu = findViewById(R.id.menu_contact_us)
        developersMenu = findViewById(R.id.menu_developers)
        logoutMenu = findViewById(R.id.menu_logout)
    }

    private fun setupClickListeners() {
        // Menu item clicks
        editProfileMenu.setOnClickListener {
            startActivity(Intent(this, EditProfileConfirmationActivity::class.java))
        }

        aboutUsMenu.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }

        contactUsMenu.setOnClickListener {
            startActivity(Intent(this, ContactUsActivity::class.java))
        }

        developersMenu.setOnClickListener {
            startActivity(Intent(this, DevelopersActivity::class.java))
        }

        logoutMenu.setOnClickListener {
            DialogUtils.showCustomConfirmationDialog(
                context = this,
                title = "Logout",
                message = "Are you sure you want to logout?",
                onConfirm = {
                    (application as FlowpayApp).loggedInuser = null
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            )
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }
}