package com.mine.flowpay

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.mine.flowpay.model.Users
import com.mine.flowpay.network.RetrofitInstance
import android.text.InputType

class RegisterActivity : AppCompatActivity() {
    private lateinit var btnRegister: Button
    private var emailIsGood = false
    private var usernameIsGood = false
    private var passwordIsGood = false
    private var confirmPasswordIsGood = false

    // Password requirement checks
    private var hasMinLength = false
    private var hasLettersNumbersSpecial = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Set up views
        val txtEmail = findViewById<EditText>(R.id.edittext_email)
        val txtUsername = findViewById<EditText>(R.id.edittext_username)
        val txtPassword = findViewById<EditText>(R.id.edittext_password)
        val txtConfirmPassword = findViewById<EditText>(R.id.edittext_confirm_password)
        btnRegister = findViewById(R.id.btn_register)
        val loginText = findViewById<TextView>(R.id.txt_login)

        // Error text views
        val emailError = findViewById<TextView>(R.id.txt_email_error)
        val usernameError = findViewById<TextView>(R.id.txt_username_error)
        val confirmPasswordError = findViewById<TextView>(R.id.txt_confirm_password_error)

        // Password requirement icons
        val iconMinLength = findViewById<ImageView>(R.id.icon_min_length)
        val iconLettersNumbersSpecial = findViewById<ImageView>(R.id.icon_letters_numbers_special)

        // Password visibility toggles
        val passwordToggle = findViewById<ImageView>(R.id.password_visibility_toggle)
        val confirmPasswordToggle = findViewById<ImageView>(R.id.confirm_password_visibility_toggle)

        // Initially hide error messages and disable button
        emailError.text = ""
        usernameError.text = ""
        confirmPasswordError.text = ""
        updateButtonState()

        // Set error text colors
        emailError.setTextColor(getColor(R.color.error_text))
        usernameError.setTextColor(getColor(R.color.error_text))
        confirmPasswordError.setTextColor(getColor(R.color.error_text))

        // Password visibility toggles
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

        confirmPasswordToggle.setOnClickListener {
            // Toggle confirm password visibility
            if (txtConfirmPassword.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                // Hide password
                txtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                confirmPasswordToggle.setImageResource(R.drawable.ic_hide)
            } else {
                // Show password
                txtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                confirmPasswordToggle.setImageResource(R.drawable.ic_unhide)
            }
            // Move cursor to the end
            txtConfirmPassword.setSelection(txtConfirmPassword.text.length)
        }

        // Email validation
        txtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().lowercase()
                // First check: Valid email format
                if (email.isEmpty()) {
                    emailError.text = "Email is required"
                    emailIsGood = false
                    updateButtonState()
                } else if (!isValidEmail(email)) {
                    emailError.text = "Invalid Email"
                    emailIsGood = false
                    updateButtonState()
                } else {
                    // Second check: Email already registered
                    lifecycleScope.launch {
                        val response = RetrofitInstance.api.getUserByEmail(email)
                        if (response.isSuccessful && response.body() != null) {
                            emailError.text = "Email already registered"
                            emailIsGood = false
                        } else {
                            emailError.text = ""
                            emailIsGood = true
                        }
                        updateButtonState()
                    }
                }
            }
        })

        // Username validation
        txtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val username = s.toString()
                if (username.isEmpty()) {
                    usernameError.text = "Username is required"
                    usernameIsGood = false
                    updateButtonState()
                } else {
                    lifecycleScope.launch {
                        val response = RetrofitInstance.api.getUserByUsername(username)
                        if (response.isSuccessful && response.body() != null) {
                            usernameError.text = "Username already taken"
                            usernameIsGood = false
                        } else {
                            usernameError.text = ""
                            usernameIsGood = true
                        }
                        updateButtonState()
                    }
                }
            }
        })

        // Password validation
        txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                validatePassword(password, iconMinLength, iconLettersNumbersSpecial)
                validateConfirmPassword(password, txtConfirmPassword.text.toString(), confirmPasswordError)
                updateButtonState()
            }
        })

        // Confirm Password validation
        txtConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateConfirmPassword(txtPassword.text.toString(), s.toString(), confirmPasswordError)
            }
        })

        // Register button click
        btnRegister.setOnClickListener {
            val email = txtEmail.text.toString().lowercase()
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()
            
            lifecycleScope.launch {
                // Final validation before registration
                var isValid = true
                
                // Check email
                if (!isValidEmail(email)) {
                    emailError.text = "Invalid Email"
                    emailIsGood = false
                    isValid = false
                } else {
                    val response = RetrofitInstance.api.getUserByEmail(email)
                    if (response.isSuccessful && response.body() != null) {
                        emailError.text = "Email already registered"
                        emailIsGood = false
                        isValid = false
                    }
                }
                
                // Check username
                if (username.isEmpty()) {
                    usernameError.text = "Username is required"
                    usernameIsGood = false
                    isValid = false
                } else {
                    val response = RetrofitInstance.api.getUserByUsername(username)
                    if (response.isSuccessful && response.body() != null) {
                        usernameError.text = "Username already taken"
                        usernameIsGood = false
                        isValid = false
                    }
                }
                
                // Update button state in case validation failed
                updateButtonState()
                
                // Proceed with registration if everything is valid
                if (isValid && passwordIsGood && confirmPasswordIsGood) {
                    val newUser = Users(
                        email = email,
                        username = username,
                        password = password
                    )
                    val registerResponse = RetrofitInstance.api.registerUser(newUser)
                    if (registerResponse.isSuccessful) {
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        // handle registration error (e.g. show error message)
                    }
                }
            }
        }

        // Redirect to login page
        loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun validatePassword(password: String, iconMinLength: ImageView, iconLettersNumbersSpecial: ImageView) {
        // Check minimum length
        hasMinLength = password.length >= 7
        iconMinLength.setImageResource(if (hasMinLength) R.drawable.ic_check_green else R.drawable.ic_dot_grey)

        // Check for letters, numbers, and special characters
        hasLettersNumbersSpecial = password.any { it.isLetter() } &&
                password.any { it.isDigit() } &&
                password.any { "!@#$%^&*()_+[]{}|;:,.<>?".contains(it) }
        iconLettersNumbersSpecial.setImageResource(
            if (hasLettersNumbersSpecial) R.drawable.ic_check_green else R.drawable.ic_dot_grey
        )

        // Update password validity
        passwordIsGood = hasMinLength && hasLettersNumbersSpecial
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String, errorText: TextView) {
        if (confirmPassword.isEmpty()) {
            errorText.text = "Confirm password is required"
            confirmPasswordIsGood = false
        } else if (password != confirmPassword) {
            errorText.text = "Passwords do not match"
            confirmPasswordIsGood = false
        } else {
            errorText.text = ""
            confirmPasswordIsGood = true
        }
        updateButtonState()
    }

    private fun updateButtonState() {
        val isValid = emailIsGood && usernameIsGood && passwordIsGood && confirmPasswordIsGood
        btnRegister.isEnabled = isValid
        btnRegister.alpha = if (isValid) 1.0f else 0.5f
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}