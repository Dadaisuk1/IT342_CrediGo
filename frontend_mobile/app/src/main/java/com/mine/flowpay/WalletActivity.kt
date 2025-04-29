package com.mine.flowpay

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.model.Deposit
import com.mine.flowpay.model.Users
import com.mine.flowpay.network.RetrofitInstance
import kotlinx.coroutines.*

class WalletActivity : AppCompatActivity() {
    // UI components
    private lateinit var backButton: ImageView
    private lateinit var balanceTextView: TextView
    private lateinit var amountEditText: EditText
    private lateinit var cashInButton: Button
    private lateinit var limitWarningText: TextView
    private lateinit var selectionErrorText: TextView

    // Payment options
    private lateinit var gcashOption: LinearLayout
    private lateinit var mayaOption: LinearLayout
    private lateinit var visaOption: LinearLayout
    private lateinit var mastercardOption: LinearLayout

    // Menu items
    private lateinit var transactionsMenu: LinearLayout
    private lateinit var depositHistoryMenu: LinearLayout

    // Selected payment option
    private var selectedPaymentOption: String? = null
    private var selectedPaymentView: LinearLayout? = null

    // Current user
    private lateinit var currentUser: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

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

        // Set up UI with user data
        updateUI()

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

        // Set up click listeners
        setupClickListeners()

        // Set up text watchers
        setupTextWatchers()

        // Set up navbar
        supportFragmentManager.beginTransaction()
            .replace(R.id.navbar_container, NavbarFragment())
            .commit()
    }

    private fun initializeViews() {
        // Toolbar and navigation
        backButton = findViewById(R.id.iv_back)

        // Balance and cash in components
        balanceTextView = findViewById(R.id.text_balance)
        amountEditText = findViewById(R.id.edit_amount)
        cashInButton = findViewById(R.id.btn_cash_in)
        limitWarningText = findViewById(R.id.text_limit_warning)
        selectionErrorText = findViewById(R.id.text_selection_error)

        // Payment options
        gcashOption = findViewById(R.id.option_gcash)
        mayaOption = findViewById(R.id.option_maya)
        visaOption = findViewById(R.id.option_visa)
        mastercardOption = findViewById(R.id.option_mastercard)

        // Menu items
        transactionsMenu = findViewById(R.id.menu_transactions)
        depositHistoryMenu = findViewById(R.id.menu_deposit_history)
    }

    private fun updateUI() {
        // Format wallet balance with commas and two decimal points
        val formattedBalance = String.format("%,.2f", currentUser.walletBalance)
        balanceTextView.text = "₱$formattedBalance"

        // Initially disable the cash in button
        cashInButton.isEnabled = false
    }

    private fun setupClickListeners() {
        // Back button click
        backButton.setOnClickListener {
            finish()
        }

        // Payment option clicks
        gcashOption.setOnClickListener {
            selectPaymentOption("GCash", gcashOption)
        }

        mayaOption.setOnClickListener {
            selectPaymentOption("Maya", mayaOption)
        }

        visaOption.setOnClickListener {
            selectPaymentOption("Visa", visaOption)
        }

        mastercardOption.setOnClickListener {
            selectPaymentOption("Mastercard", mastercardOption)
        }

        // Cash in button click
        cashInButton.setOnClickListener {
            val amountText = amountEditText.text.toString()
            if (amountText.isNotEmpty() && selectedPaymentOption != null) {
                try {
                    val amount = amountText.toDouble()
                    if (amount > 0) {
                        // Check if new balance would exceed 1,000,000
                        val newBalance = currentUser.walletBalance + amount
                        if (newBalance > 1000000) {
                            // Already showing warning via text watcher
                        } else {
                            // Disable button to prevent multiple clicks
                            cashInButton.isEnabled = false

                            // Create deposit instance
                            createDeposit(amount)

                            // Clear input field and selection
                            amountEditText.text.clear()
                            deselectAllPaymentOptions()

                            // Reset error message states
                            limitWarningText.visibility = View.INVISIBLE
                            selectionErrorText.visibility = View.INVISIBLE
                        }
                    } else {
                        Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (selectedPaymentOption == null) {
                    selectionErrorText.visibility = View.VISIBLE
                }
                if (amountText.isEmpty()) {
                    Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Menu item clicks
        transactionsMenu.setOnClickListener {
            startActivity(Intent(this, TransactionActivity::class.java))
        }

        depositHistoryMenu.setOnClickListener {
            startActivity(Intent(this, DepositHistoryActivity::class.java))
        }
    }

    private fun setupTextWatchers() {
        // Ensure error message spaces are always maintained
        limitWarningText.visibility = View.INVISIBLE
        selectionErrorText.visibility = View.INVISIBLE
        
        amountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                val amountText = s.toString()
                if (amountText.isNotEmpty()) {
                    try {
                        val amount = amountText.toDouble()
                        val newBalance = currentUser.walletBalance + amount
                        
                        // Check if new balance exceeds limit
                        if (newBalance > 1000000) {
                            limitWarningText.visibility = View.VISIBLE
                            selectionErrorText.visibility = View.INVISIBLE
                            cashInButton.isEnabled = false
                        } else {
                            limitWarningText.visibility = View.INVISIBLE
                            // Show selection error if amount entered but no option selected
                            selectionErrorText.visibility = if (selectedPaymentOption == null) View.VISIBLE else View.INVISIBLE
                            checkCashInButtonStatus()
                        }
                    } catch (e: NumberFormatException) {
                        limitWarningText.visibility = View.INVISIBLE
                        selectionErrorText.visibility = View.INVISIBLE
                        checkCashInButtonStatus()
                    }
                } else {
                    limitWarningText.visibility = View.INVISIBLE
                    selectionErrorText.visibility = View.INVISIBLE
                    checkCashInButtonStatus()
                }
            }
        })
    }

    private fun selectPaymentOption(option: String, view: LinearLayout) {
        // Deselect previous option
        selectedPaymentView?.setBackgroundResource(R.drawable.item_background)
        
        // Select new option
        selectedPaymentOption = option
        selectedPaymentView = view
        view.setBackgroundResource(R.drawable.item_wallet_option_selected)
        
        // Hide selection error
        selectionErrorText.visibility = View.INVISIBLE
        
        // Enable cash in button if amount is entered
        checkCashInButtonStatus()
    }

    private fun deselectAllPaymentOptions() {
        // Reset all payment options
        val options = listOf(gcashOption, mayaOption, visaOption, mastercardOption)
        for (option in options) {
            option.setBackgroundResource(R.drawable.item_background)
        }
        
        selectedPaymentOption = null
        selectedPaymentView = null
        
        // Show selection error if amount is entered
        if (amountEditText.text.toString().isNotEmpty()) {
            selectionErrorText.visibility = View.VISIBLE
        }
        
        // Disable cash in button
        cashInButton.isEnabled = false
    }

    private fun checkCashInButtonStatus() {
        val amountText = amountEditText.text.toString()
        val isEnabled = amountText.isNotEmpty() &&
                selectedPaymentOption != null &&
                limitWarningText.visibility != View.VISIBLE
        cashInButton.isEnabled = isEnabled

        // Change button appearance based on enabled state
        if (isEnabled) {
            cashInButton.alpha = 1.0f
        } else {
            cashInButton.alpha = 0.5f
        }
    }

    private fun createDeposit(amount: Double) {
        lifecycleScope.launch {
            if (selectedPaymentOption == null) {
                Toast.makeText(this@WalletActivity, "Please select a payment method", Toast.LENGTH_SHORT).show()
                return@launch
            }
            try {
                val deposit = Deposit(userId = currentUser.user_id, amount = amount, paymentOption = selectedPaymentOption!!)
                val depositResponse = RetrofitInstance.api.createDeposit(deposit)
                if (depositResponse.isSuccessful) {
                    // Update user balance via API
                    val newBalance = currentUser.walletBalance + amount
                    val updateBalanceResponse = RetrofitInstance.api.updateUserBalance(currentUser.user_id, newBalance)
                    if (updateBalanceResponse.isSuccessful) {
                        currentUser.walletBalance = newBalance
                        (application as FlowpayApp).loggedInuser = currentUser
                        updateUI()
                        Toast.makeText(this@WalletActivity, "Deposit successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@WalletActivity, "Failed to update balance", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@WalletActivity, "Error creating deposit", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@WalletActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // No coroutineScope.cancel() needed as we use lifecycleScope
    }
}
