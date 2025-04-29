package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.data.Users
import com.mine.flowpay.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    // ... existing properties ...

    override fun onCreate(savedInstanceState: Bundle?) {
        // ... existing onCreate implementation ...
    }

    private fun initializeViews() {
        // ... existing initializeViews implementation ...
    }

    private fun updateUI() {
        // ... existing updateUI implementation ...
    }

    private fun setupClickListeners() {
        // ... existing click listeners ...

        logoutMenu.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        btnCashIn.setOnClickListener {
            val amountText = editAmount.text.toString()
            val amount = amountText.toDoubleOrNull() ?: 0.0
            val newBalance = currentBalance + amount
            
            if (newBalance > 1000000) {
                Toast.makeText(this, "Balance limit is 1,000,000", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with cash in logic
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm, null)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.tv_dialog_message)
        dialogMessage.text = "Are you sure you want to logout?"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Set up confirm button
        dialogView.findViewById<Button>(R.id.btn_confirm).apply {
            text = "Logout"
            setOnClickListener {
                // Log out the user
                (application as FlowpayApp).loggedInuser = null

                // Navigate to login screen
                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                finish()
                dialog.dismiss()
            }
        }

        // Set up cancel button
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Implement balance logic with max limit and formatted display
    private fun updateBalance(newBalance: Double) {
        if (newBalance > 1000000) {
            Toast.makeText(this, "Balance max limit at 1,000,000", Toast.LENGTH_SHORT).show()
        } else {
            // Update balance logic here
        }
    }

    private fun formatNumber(number: Double): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
    }

    // Update balance display
    balanceView.text = formatNumber(currentBalance)
    editAmount.setText(formatNumber(0.0))
}