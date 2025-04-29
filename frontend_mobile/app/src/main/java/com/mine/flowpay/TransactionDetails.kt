package com.mine.flowpay

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mine.flowpay.fragments.NavbarFragment
import com.mine.flowpay.model.Transaction
import com.mine.flowpay.model.Product
import com.mine.flowpay.model.ProductCategory
import com.mine.flowpay.network.RetrofitInstance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

class TransactionDetails : AppCompatActivity() {
    companion object {
        private const val MAIL_LIST_REQUEST_CODE = 100
    }

    // UI components
    private lateinit var backButton: ImageView
    private lateinit var productNameView: TextView
    private lateinit var priceView: TextView
    private lateinit var transactionIdView: TextView
    private lateinit var categoryView: TextView
    private lateinit var dateView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_details)

        // Set header title
        val headerTitle = findViewById<TextView>(R.id.tv_header_title)
        headerTitle.text = "Transaction Details"

        // Set up navbar
        supportFragmentManager.beginTransaction()
            .replace(R.id.navbar_container, NavbarFragment())
            .commit()

        // Initialize views
        backButton = findViewById(R.id.iv_back)
        productNameView = findViewById(R.id.tv_product_name_header)
        priceView = findViewById(R.id.tv_price_header)
        transactionIdView = findViewById(R.id.tv_transaction_id_value)
        categoryView = findViewById(R.id.tv_category_value)
        dateView = findViewById(R.id.tv_date_value)

        // Get transaction ID from intent
        val transactionId = intent.getLongExtra("TRANSACTION_ID", -1)
        if (transactionId != -1L) {
            loadTransactionDetails(transactionId)
        }

        // Set header title and back button
        findViewById<ImageView>(R.id.iv_back).setOnClickListener { onBackPressed() }

        // Set up navbar fragment with FlowpayApp instance
        val app = application as FlowpayApp
        val navbarFragment = NavbarFragment().apply {
            arguments = Bundle().apply {
                putLong("USER_ID", app.loggedInuser?.user_id ?: -1)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.navbar_container, navbarFragment)
            .commit()
    }

    private fun loadTransactionDetails(transactionId: Long) {
        lifecycleScope.launch {
            val transactionResponse = RetrofitInstance.api.getTransactionById(transactionId)
            if (transactionResponse.isSuccessful) {
                val transaction = transactionResponse.body()
                if (transaction != null) {
                    productNameView.text = transaction.type
                    priceView.text = "₱${String.format("%.2f", transaction.amount)}"
                    transactionIdView.text = transaction.transaction_id.toString()
                    // Get product and category info from backend
                    val productResponse = RetrofitInstance.api.getProductByName(transaction.type)
                    if (productResponse.isSuccessful) {
                        val product = productResponse.body()
                        if (product != null) {
                            val categoryResponse = RetrofitInstance.api.getCategoryById(product.category_id)
                            if (categoryResponse.isSuccessful) {
                                val category = categoryResponse.body()
                                categoryView.text = category?.category_name ?: "Unknown Category"
                            } else {
                                categoryView.text = "Unknown Category"
                            }
                        } else {
                            // Try to parse category from transaction type
                            val parts = transaction.type.split(" - ")
                            if (parts.size > 1) {
                                productNameView.text = parts[0]
                                categoryView.text = parts[1]
                            } else {
                                categoryView.text = "Unknown Category"
                            }
                        }
                    } else {
                        categoryView.text = "Unknown Category"
                    }
                    // Format date
                    val dateFormat = SimpleDateFormat("MM/dd/yy HH:mm", Locale.getDefault())
                    dateView.text = dateFormat.format(Date(transaction.timestamp))
                }
            }
        }
    }
}
