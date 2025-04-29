package com.mine.flowpay

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.adapters.ProductAdapter
import com.mine.flowpay.model.Mail
import com.mine.flowpay.model.Product
import com.mine.flowpay.model.ProductCategory
import com.mine.flowpay.model.Transaction
import com.mine.flowpay.model.Wishlist
import com.mine.flowpay.network.RetrofitInstance
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val MAIL_LIST_REQUEST_CODE = 100
    }

    private lateinit var searchInput: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var clearSearchButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var contentContainer: View
    private lateinit var emptyStateContainer: View
    private lateinit var walletBalanceView: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var mailIcon: ImageView
    private lateinit var confirmationPanel: CardView
    private lateinit var selectedProductView: TextView
    private lateinit var selectedPriceView: TextView
    private lateinit var buyButton: Button
    private lateinit var errorMessageView: TextView

    private var currentUserId: Long = -1
    private val wishlistedProducts = mutableSetOf<Long>()
    private val searchResults = mutableListOf<Product>()
    private val categoryResults = mutableListOf<ProductCategory>()
    private var selectedProduct: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Get user ID from FlowpayApp
        val app = application as FlowpayApp
        currentUserId = app.loggedInuser?.user_id ?: -1

        // Initialize views
        searchInput = findViewById(R.id.et_search)
        searchIcon = findViewById(R.id.iv_search)
        clearSearchButton = findViewById(R.id.iv_clear_search)
        recyclerView = findViewById(R.id.rv_products)
        contentContainer = findViewById(R.id.content_container)
        emptyStateContainer = findViewById(R.id.empty_state_container)
        walletBalanceView = findViewById(R.id.tv_balance)
        walletIcon = findViewById(R.id.iv_wallet)
        mailIcon = findViewById(R.id.mail_icon)
        confirmationPanel = findViewById(R.id.confirmation_panel)
        selectedProductView = findViewById(R.id.tv_selected_product)
        selectedPriceView = findViewById(R.id.tv_selected_price)
        buyButton = findViewById(R.id.btn_buy)
        errorMessageView = findViewById(R.id.tv_error_message)

        // Set wallet balance
        val balance = app.loggedInuser?.walletBalance ?: 0.0
        val formattedBalance = String.format("%,.2f", balance)
        walletBalanceView.text = "₱$formattedBalance"

        // Set up wallet icon click listener
        walletIcon.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        // Set up mail icon click listener
        mailIcon.setOnClickListener {
            startActivityForResult(Intent(this, MailsActivity::class.java), MAIL_LIST_REQUEST_CODE)
        }

        // Set up buy button
        buyButton.setOnClickListener {
            purchaseSelectedProduct()
        }

        // Set up search functionality
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchInput.text.toString())
                true
            } else {
                false
            }
        }
        clearSearchButton.setOnClickListener {
            clearSearch()
        }
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Load all products initially from backend
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getAllProducts()
            if (response.isSuccessful) {
                val products = response.body() ?: emptyList()
                searchResults.clear()
                searchResults.addAll(products)
                updateSearchResults()
            }
        }
    }

    private fun performSearch(query: String) {
        lifecycleScope.launch {
            val response = RetrofitInstance.api.searchProducts(query)
            if (response.isSuccessful) {
                val products = response.body() ?: emptyList()
                searchResults.clear()
                searchResults.addAll(products)
                updateSearchResults()
            }
        }
    }

    private fun clearSearch() {
        searchInput.setText("")
        clearSearchButton.visibility = View.GONE
        // Reload all products from backend
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getAllProducts()
            if (response.isSuccessful) {
                val products = response.body() ?: emptyList()
                searchResults.clear()
                searchResults.addAll(products)
                updateSearchResults()
            }
        }
    }

    private fun updateEmptyState() {
        if (searchResults.isEmpty()) {
            // Show empty state
            emptyStateContainer.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            // Show results
            emptyStateContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun updateSearchResults() {
        // If we have product results, show them
        if (searchResults.isNotEmpty()) {
            recyclerView.adapter = ProductAdapter(
                searchResults,
                onProductSelected = { product -> onProductSelected(product) },
                onWishlistClicked = { product -> toggleWishlist(product) },
                isInWishlist = { product -> wishlistedProducts.contains(product.product_id) }
            )
        } else {
            // No results
            recyclerView.adapter = ProductAdapter(
                emptyList(),
                onProductSelected = { product -> onProductSelected(product) },
                onWishlistClicked = { product -> toggleWishlist(product) },
                isInWishlist = { product -> wishlistedProducts.contains(product.product_id) }
            )
        }

        // Update empty state visibility
        updateEmptyState()
    }

    private fun onProductSelected(product: Product) {
        if (selectedProduct == product) {
            hideConfirmationPanel()
            selectedProduct = null
            (recyclerView.adapter as? ProductAdapter)?.apply {
                setSelectedProduct(null)
                notifyDataSetChanged() // Force immediate UI update
            }
        } else {
            showConfirmationPanel(product)
            selectedProduct = product
            (recyclerView.adapter as? ProductAdapter)?.setSelectedProduct(product)
        }
    }

    private fun toggleWishlist(product: Product) {
        lifecycleScope.launch {
            if (wishlistedProducts.contains(product.product_id)) {
                val response = RetrofitInstance.api.removeFromWishlist(currentUserId, product.product_id)
                if (response.isSuccessful) {
                    wishlistedProducts.remove(product.product_id)
                    (recyclerView.adapter as? ProductAdapter)?.updateWishlistState(product)
                    Toast.makeText(this@SearchActivity, "Removed from wishlist", Toast.LENGTH_SHORT).show()
                }
            } else {
                val wishlist = Wishlist(userid = currentUserId, productid = product.product_id)
                val response = RetrofitInstance.api.addToWishlist(wishlist)
                if (response.isSuccessful) {
                    wishlistedProducts.add(product.product_id)
                    (recyclerView.adapter as? ProductAdapter)?.updateWishlistState(product)
                    Toast.makeText(this@SearchActivity, "Added to wishlist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun purchaseSelectedProduct() {
        selectedProduct?.let { product ->
            val user = (application as FlowpayApp).loggedInuser
            if (user != null && user.walletBalance >= product.price) {
                lifecycleScope.launch {
                    try {
                        // Create transaction via API
                        val transaction = Transaction(userId = currentUserId, type = product.productName, amount = product.price, timestamp = System.currentTimeMillis())
                        val transactionResponse = RetrofitInstance.api.createTransaction(transaction)
                        if (!transactionResponse.isSuccessful) throw Exception("Transaction failed")
                        val transactionId = transactionResponse.body()?.transaction_id ?: throw Exception("No transaction ID")
                        // Update user balance via API
                        val updateBalanceResponse = RetrofitInstance.api.updateUserBalance(currentUserId, user.walletBalance - product.price)
                        if (!updateBalanceResponse.isSuccessful) throw Exception("Failed to update balance")
                        user.walletBalance -= product.price
                        // Get category name from backend
                        val categoryResponse = RetrofitInstance.api.getCategoryById(product.category_id)
                        val categoryName = categoryResponse.body()?.category_name ?: "Unknown"
                        // Generate random code
                        val code = generateRandomCode()
                        // Create mail via API
                        val mail = Mail(user_id = currentUserId, transaction_id = transactionId, subject = "Purchase of ${categoryName}'s ${product.productName}", message = "Hi there,\nThanks for your recent purchase of ${product.productName} from ${categoryName}. We hope you enjoy your experience!\n\nHeres the code for your game:\nCode: ${code}\n\nUse this code to top up your account!\n\nBest regards,\n— The FlowPay Team", timestamp = System.currentTimeMillis(), isRead = false)
                        val mailResponse = RetrofitInstance.api.createMail(mail)
                        if (!mailResponse.isSuccessful) throw Exception("Failed to send mail")
                        walletBalanceView.text = "₱${String.format("%,.2f", user.walletBalance)}"
                        hideConfirmationPanel()
                        Toast.makeText(this@SearchActivity, "Purchase successful!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@SearchActivity, "Error processing purchase", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationPanel(product: Product) {
        confirmationPanel.visibility = View.VISIBLE
        selectedProductView.text = product.productName
        selectedPriceView.text = "₱${product.price}"
        errorMessageView.visibility = View.GONE
        val user = (application as FlowpayApp).loggedInuser
        if (user != null && user.walletBalance >= product.price) {
            buyButton.isEnabled = true
            buyButton.alpha = 1.0f
        } else {
            buyButton.isEnabled = false
            buyButton.alpha = 0.5f
            errorMessageView.visibility = View.VISIBLE
        }
    }

    private fun hideConfirmationPanel() {
        confirmationPanel.visibility = View.GONE
    }

    private fun generateRandomCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = java.util.Random()
        val firstPart = (1..3)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
        val secondPart = (1..3)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
        return "$firstPart-$secondPart"
    }
}
