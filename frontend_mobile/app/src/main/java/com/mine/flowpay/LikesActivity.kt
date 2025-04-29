package com.mine.flowpay

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.adapters.ProductAdapter
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.model.Mail
import com.mine.flowpay.model.Product
import com.mine.flowpay.model.ProductCategory
import com.mine.flowpay.model.Transaction
import com.mine.flowpay.model.Wishlist
import com.mine.flowpay.network.RetrofitInstance
import com.mine.flowpay.utils.DialogUtils
import kotlinx.coroutines.launch

class LikesActivity : AppCompatActivity() {
    companion object {
        private const val MAIL_LIST_REQUEST_CODE = 100
    }

    private lateinit var recyclerView: RecyclerView
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
    private val likedProducts = mutableListOf<Product>()
    private var selectedProduct: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)

        // Get user ID from FlowpayApp
        val app = application as FlowpayApp
        currentUserId = app.loggedInuser?.user_id ?: -1

        // Initialize views
        recyclerView = findViewById(R.id.rv_liked_products)
        walletBalanceView = findViewById(R.id.tv_balance)
        walletIcon = findViewById(R.id.iv_wallet)
        mailIcon = findViewById(R.id.mail_icon)
        confirmationPanel = findViewById(R.id.confirmation_panel)
        selectedProductView = findViewById(R.id.tv_selected_product)
        selectedPriceView = findViewById(R.id.tv_selected_price)
        buyButton = findViewById(R.id.btn_buy)
        errorMessageView = findViewById(R.id.tv_error_message)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        confirmationPanel.visibility = View.GONE
        errorMessageView.visibility = View.GONE

        // Set wallet balance
        val user = app.loggedInuser
        if (user != null) {
            walletBalanceView.text = "₱${String.format("%,.2f", user.walletBalance)}"
        }

        // Load liked products and wishlist from backend
        lifecycleScope.launch {
            val likedResponse = RetrofitInstance.api.getLikedProducts(currentUserId)
            val wishlistResponse = RetrofitInstance.api.getWishlist(currentUserId)
            if (likedResponse.isSuccessful && wishlistResponse.isSuccessful) {
                val liked = likedResponse.body() ?: emptyList()
                val wishlist = wishlistResponse.body() ?: emptyList()
                likedProducts.clear()
                likedProducts.addAll(liked)
                wishlistedProducts.clear()
                wishlistedProducts.addAll(wishlist.map { it.product_id })
                updateLikedProducts(likedProducts)
            }
        }

        // Set up click listeners
        walletIcon.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }

        mailIcon.setOnClickListener {
            startActivityForResult(Intent(this, MailsActivity::class.java), MAIL_LIST_REQUEST_CODE)
        }
    }

    private fun updateLikedProducts(allProducts: List<Product>) {
        recyclerView.adapter = ProductAdapter(
            allProducts,
            onProductSelected = { product -> onProductSelected(product) },
            onWishlistClicked = { product -> toggleWishlist(product) },
            isInWishlist = { product -> wishlistedProducts.contains(product.product_id) }
        )
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

    private fun showConfirmationPanel(product: Product) {
        confirmationPanel.visibility = View.VISIBLE
        selectedProductView.text = "${product.productName} • E-Wallet"
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
        buyButton.setOnClickListener {
            purchaseSelectedProduct()
        }
    }

    private fun hideConfirmationPanel() {
        confirmationPanel.visibility = View.GONE
    }

    private fun toggleWishlist(product: Product) {
        lifecycleScope.launch {
            if (wishlistedProducts.contains(product.product_id)) {
                val response = RetrofitInstance.api.removeFromWishlist(currentUserId, product.product_id)
                if (response.isSuccessful) {
                    wishlistedProducts.remove(product.product_id)
                    (recyclerView.adapter as? ProductAdapter)?.updateWishlistState(product)
                    Toast.makeText(this@LikesActivity, "Removed from wishlist", Toast.LENGTH_SHORT).show()
                }
            } else {
                val wishlist = Wishlist(userid = currentUserId, productid = product.product_id)
                val response = RetrofitInstance.api.addToWishlist(wishlist)
                if (response.isSuccessful) {
                    wishlistedProducts.add(product.product_id)
                    (recyclerView.adapter as? ProductAdapter)?.updateWishlistState(product)
                    Toast.makeText(this@LikesActivity, "Added to wishlist", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@LikesActivity, "Purchase successful!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@LikesActivity, "Error processing purchase", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show()
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAIL_LIST_REQUEST_CODE && resultCode == RESULT_OK) {
            // Force refresh mail count
            // mailViewModel.updateUnreadMailCount()
        }
    }
}
