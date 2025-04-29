package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mine.flowpay.app.FlowpayApp

class EditProfileConfirmationActivity : AppCompatActivity() {
    private lateinit var btnContinue: Button
    private lateinit var txtPasswordError: TextView
    private var passwordIsGood = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_confirmation)

        // Get current user
        val currentUser = (application as FlowpayApp).loggedInuser
            ?: run {
                finish()
                return
            }

        // Set up views
        val txtPassword = findViewById<EditText>(R.id.edittext_password)
        btnContinue = findViewById(R.id.btn_continue)
        txtPasswordError = findViewById(R.id.txt_password_error)
        val txtCancel = findViewById<TextView>(R.id.txt_cancel)
        val btnBack = findViewById<ImageView>(R.id.iv_back)

        // Hide error message initially
        txtPasswordError.text = ""
        txtPasswordError.setTextColor(getColor(R.color.error_text))

        // Initially disable button
        updateButtonState()

        // Password validation
        txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isEmpty()) {
                    txtPasswordError.text = "Password is required"
                    passwordIsGood = false
                } else if (password != currentUser.password) {
                    txtPasswordError.text = "Incorrect password"
                    passwordIsGood = false
                } else {
                    txtPasswordError.text = ""
                    passwordIsGood = true
                }
                updateButtonState()
            }
        })

        // Continue button click
        btnContinue.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            finish()
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

    private fun updateButtonState() {
        btnContinue.isEnabled = passwordIsGood
        btnContinue.alpha = if (passwordIsGood) 1.0f else 0.5f
    }
}