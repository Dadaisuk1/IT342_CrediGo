package com.mine.flowpay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.mine.flowpay.data.Users
import com.mine.flowpay.data.database.AppDatabase
import com.mine.flowpay.data.repository.UserRepository
import com.mine.flowpay.app.FlowpayApp
import kotlinx.coroutines.launch

class TestActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository
    
    // Create User Views
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    
    // Update User Views
    private lateinit var etUpdateEmail: EditText
    private lateinit var etUpdateUsername: EditText
    private lateinit var etUpdatePassword: EditText
    
    // Delete User View
    private lateinit var etDeleteId: EditText
    
    // Display Views
    private lateinit var tvUsersList: TextView
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // Initialize repository using the application's database instance
        val database = (application as FlowpayApp).database
        userRepository = UserRepository(database.userDao())

        initializeViews()
        setupClickListeners()
        refreshUsersList() // Load initial users list
    }

    private fun initializeViews() {
        // Create User Views
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        
        // Update User Views
        etUpdateEmail = findViewById(R.id.etUpdateEmail)
        etUpdateUsername = findViewById(R.id.etUpdateUsername)
        etUpdatePassword = findViewById(R.id.etUpdatePassword)
        
        // Delete User View
        etDeleteId = findViewById(R.id.etDeleteId)
        
        // Display Views
        tvUsersList = findViewById(R.id.tvUsersList)
        tvStatus = findViewById(R.id.tvStatus)
    }

    private fun setupClickListeners() {
        findViewById<Button>(R.id.btnCreate).setOnClickListener { createUser() }
        findViewById<Button>(R.id.btnUpdate).setOnClickListener { updateUser() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { deleteUser() }
    }

    private fun createUser() {
        val email = etEmail.text.toString()
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (email.isBlank() || username.isBlank() || password.isBlank()) {
            updateStatus("Please fill all fields", isError = true)
            return
        }

        lifecycleScope.launch {
            try {
                val newUser = Users(
                    email = email,
                    username = username,
                    password = password
                )
                userRepository.insertUser(newUser)
                updateStatus("User created successfully!")
                refreshUsersList()
                clearCreateFields()
            } catch (e: Exception) {
                updateStatus("Create Error: ${e.message}", isError = true)
            }
        }
    }

    private fun refreshUsersList() {
        lifecycleScope.launch {
            try {
                val users = userRepository.getAllUsers()
                val result = StringBuilder()
                users.forEach { user ->
                    result.append("ID: ${user.user_id}\nUsername: ${user.username}\nEmail: ${user.email}\n\n")
                }
                tvUsersList.text = if (result.isNotEmpty()) result else "No users found"
                
                // If users exist, pre-fill update fields with last user's data
                if (users.isNotEmpty()) {
                    val lastUser = users.last()
                    prefillUpdateFields(lastUser)
                }
            } catch (e: Exception) {
                updateStatus("Error loading users: ${e.message}", isError = true)
            }
        }
    }

    private fun updateUser() {
        lifecycleScope.launch {
            try {
                val users = userRepository.getAllUsers()
                if (users.isNotEmpty()) {
                    val lastUser = users.last()
                    lastUser.apply {
                        email = etUpdateEmail.text.toString()
                        username = etUpdateUsername.text.toString()
                        password = etUpdatePassword.text.toString()
                    }
                    userRepository.updateUser(lastUser)
                    updateStatus("User updated successfully!")
                    refreshUsersList()
                } else {
                    updateStatus("No users to update!", isError = true)
                }
            } catch (e: Exception) {
                updateStatus("Update Error: ${e.message}", isError = true)
            }
        }
    }

    private fun deleteUser() {
        val userId = etDeleteId.text.toString().toLongOrNull()
        if (userId == null) {
            updateStatus("Please enter a valid user ID", isError = true)
            return
        }

        lifecycleScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    userRepository.deleteUser(user)
                    updateStatus("User deleted successfully!")
                    refreshUsersList()
                    etDeleteId.text.clear()
                } else {
                    updateStatus("User not found", isError = true)
                }
            } catch (e: Exception) {
                updateStatus("Delete Error: ${e.message}", isError = true)
            }
        }
    }

    private fun prefillUpdateFields(user: Users) {
        etUpdateEmail.setText(user.email)
        etUpdateUsername.setText(user.username)
        etUpdatePassword.setText(user.password)
    }

    private fun clearCreateFields() {
        etEmail.text.clear()
        etUsername.text.clear()
        etPassword.text.clear()
    }

    private fun updateStatus(message: String, isError: Boolean = false) {
        Log.d("TestActivity", message)
        tvStatus.apply {
            text = message
            setTextColor(resources.getColor(if (isError) android.R.color.holo_red_dark else android.R.color.holo_green_dark))
        }
    }
}
