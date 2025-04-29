package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.model.Users
import com.mine.flowpay.network.RetrofitInstance
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var txtEmailError: TextView
    private lateinit var txtPasswordError: TextView
    private var emailIsGood = false
    private var passwordIsGood = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
        val app = application as FlowpayApp
        if (app.sessionManager.isLoggedIn()) {
            // User is already logged in, redirect to home
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        // Set up views
        val txtEmail = findViewById<EditText>(R.id.edittext_email)
        val txtPassword = findViewById<EditText>(R.id.edittext_password)
        val passwordToggle = findViewById<ImageView>(R.id.password_visibility_toggle)
        btnLogin = findViewById(R.id.btn_login)
        txtEmailError = findViewById(R.id.txt_email_error)
        txtPasswordError = findViewById(R.id.txt_password_error)
        val txtRegister = findViewById<TextView>(R.id.txt_register)

        // Initially hide error messages
        txtEmailError.text = ""
        txtPasswordError.text = ""
        txtEmailError.setTextColor(getColor(R.color.error_text))
        txtPasswordError.setTextColor(getColor(R.color.error_text))

        // Initially disable button
        updateButtonState()

        // Email validation
        txtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().lowercase()
                lifecycleScope.launch {
                    if (email.isEmpty()) {
                        txtEmailError.text = "Email is required"
                        emailIsGood = false
                    } else if (!isValidEmail(email)) {
                        txtEmailError.text = "Invalid Email"
                        emailIsGood = false
                    } else {
                        // For login, we don't care if the email is registered or not at this point
                        // We'll check that when the button is pressed
                            txtEmailError.text = ""
                            emailIsGood = true
                    }
                    updateButtonState()
                }
            }
        })

        // Password validation
        txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isEmpty()) {
                    txtPasswordError.text = "Password is required"
                    passwordIsGood = false
                } else {
                    txtPasswordError.text = ""
                    passwordIsGood = true
                }
                updateButtonState()
            }
        })

        // Password visibility toggle
        passwordToggle.setOnClickListener {
            // Toggle password visibility
            if (txtPassword.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                // Hide password
                txtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_hide)
            } else {
                // Show password
                txtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_unhide)
            }
            // Move cursor to the end
            txtPassword.setSelection(txtPassword.text.length)
        }

        // Login button click
        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString().lowercase()
            val password = txtPassword.text.toString()

            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.getUserByEmail(email)
                    val user = if (response.isSuccessful) response.body() else null
                    if (user == null) {
                        txtEmailError.text = "Email not registered"
                        emailIsGood = false
                        updateButtonState()
                    } else if (user.password != password) {
                        txtPasswordError.text = "Incorrect password"
                        passwordIsGood = false
                        updateButtonState()
                    } else {
                        // Set current user in FlowpayApp
                        (application as FlowpayApp).apply {
                            loggedInuser = user
                            isLoggedIn = true
                        }
                        // Navigate to home screen
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    }
                } catch (e: Exception) {
                    txtPasswordError.text = "Login error: ${e.message}"
                }
            }
        }

        // Redirect to register page
        txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun updateButtonState() {
        val isValid = emailIsGood && passwordIsGood
        btnLogin.isEnabled = isValid
        btnLogin.alpha = if (isValid) 1.0f else 0.5f
    }
}