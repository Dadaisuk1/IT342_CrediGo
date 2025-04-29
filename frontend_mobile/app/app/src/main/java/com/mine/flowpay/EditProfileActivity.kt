package com.mine.flowpay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.data.repository.UserRepository
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {
    private lateinit var btnUpdate: Button
    private lateinit var userRepository: UserRepository
    private lateinit var txtUsernameError: TextView
    private lateinit var txtPassword: EditText
    private var usernameIsGood = false
    private var passwordIsGood = false

    // Password requirement checks
    private var hasMinLength = false
    private var hasLettersNumbersSpecial = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Get current user and repository
        val currentUser = (application as FlowpayApp).loggedInuser
            ?: run {
                finish()
                return
            }
        userRepository = UserRepository((application as FlowpayApp).database.userDao())

        // Set up views
        val txtEmail = findViewById<TextView>(R.id.txt_email)
        val txtUsername = findViewById<EditText>(R.id.edittext_username)
        txtPassword = findViewById(R.id.edittext_password)
        btnUpdate = findViewById(R.id.btn_update)
        txtUsernameError = findViewById(R.id.txt_username_error)
        val txtCancel = findViewById<TextView>(R.id.txt_cancel)
        val btnBack = findViewById<ImageView>(R.id.iv_back)

        // Password requirement icons
        val iconMinLength = findViewById<ImageView>(R.id.icon_min_length)
        val iconLettersNumbersSpecial = findViewById<ImageView>(R.id.icon_letters_numbers_special)

        // Display current email
        txtEmail.text = currentUser.email
        txtUsername.setText(currentUser.username)

        // Hide error messages initially
        txtUsernameError.text = ""
        txtUsernameError.setTextColor(getColor(R.color.error_text))

        // Initially enable button if username is unchanged
        usernameIsGood = true
        updateButtonState()

        // Username validation
        txtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val username = s.toString()
                lifecycleScope.launch {
                    if (username.isEmpty()) {
                        txtUsernameError.text = "Username is required"
                        usernameIsGood = false
                    } else if (username != currentUser.username) {
                        val existingUser = userRepository.getUserByUsername(username)
                        if (existingUser != null) {
                            txtUsernameError.text = "Username already taken"
                            usernameIsGood = false
                        } else {
                            txtUsernameError.text = ""
                            usernameIsGood = true
                        }
                    } else {
                        txtUsernameError.text = ""
                        usernameIsGood = true
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
                    passwordIsGood = true // Password is optional
                } else {
                    validatePassword(password, iconMinLength, iconLettersNumbersSpecial)
                }
                updateButtonState()
            }
        })

        // Update button click
        btnUpdate.setOnClickListener {
            lifecycleScope.launch {
                val newUsername = txtUsername.text.toString()
                val newPassword = txtPassword.text.toString()

                // Update user data
                currentUser.apply {
                    username = newUsername
                    if (newPassword.isNotEmpty()) {
                        password = newPassword
                    }
                }

                // Update in database
                userRepository.updateUser(currentUser)

                // Update in app
                (application as FlowpayApp).loggedInuser = currentUser

                // Show success message and finish
                Toast.makeText(this@EditProfileActivity, "Profile has been updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Cancel button click
        txtCancel.setOnClickListener {
            finish()
        }

        // Back button click
        btnBack.setOnClickListener {
            finish()
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

    private fun updateButtonState() {
        val isValid = usernameIsGood && (passwordIsGood || txtPassword.text.isEmpty())
        btnUpdate.isEnabled = isValid
        btnUpdate.alpha = if (isValid) 1.0f else 0.5f
    }
}