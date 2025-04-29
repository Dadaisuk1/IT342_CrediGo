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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.adapters.ProductAdapter
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.model.Product
import com.mine.flowpay.model.Wishlist
import com.mine.flowpay.model.Transaction
import com.mine.flowpay.model.Mail
import com.mine.flowpay.model.ProductCategory
import com.mine.flowpay.network.RetrofitInstance
import com.mine.flowpay.utils.DialogUtils
import com.mine.flowpay.utils.SortButtonsHandler

class ProductsListActivity : AppCompatActivity() {
    companion object {
        private const val MAIL_LIST_REQUEST_CODE = 100
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryTitleView: TextView
    private lateinit var confirmationPanel: CardView
    private lateinit var selectedProductView: TextView
    private lateinit var selectedPriceView: TextView
    private lateinit var buyButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var walletBalanceView: TextView
    private lateinit var errorMessageView: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var mailIcon: ImageView
    private lateinit var categoryBackgroundView: ImageView

    private var currentUserId: Long = -1
    private var selectedProduct: Product? = null
    private val wishlistedProducts = mutableSetOf<Long>()
    private var categoryProducts = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)

        // Get user ID and category details from intent
        currentUserId = intent.getLongExtra("USER_ID", -1)
        val categoryId = intent.getLongExtra("CATEGORY_ID", -1)
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Products"

        // Initialize views
        categoryTitleView = findViewById(R.id.tv_category_title)
        recyclerView = findViewById(R.id.rv_products)
        confirmationPanel = findViewById(R.id.confirmation_panel)
        selectedProductView = findViewById(R.id.tv_selected_product)
        selectedPriceView = findViewById(R.id.tv_selected_price)
        buyButton = findViewById(R.id.btn_buy)
        backButton = findViewById(R.id.btn_back)
        walletBalanceView = findViewById(R.id.tv_wallet_balance)
        errorMessageView = findViewById(R.id.tv_error_message)
        walletIcon = findViewById(R.id.iv_wallet)
        mailIcon = findViewById(R.id.mail_icon)
        categoryBackgroundView = findViewById(R.id.iv_category_bg)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        confirmationPanel.visibility = View.GONE
        errorMessageView.visibility = View.GONE

        // Set wallet balance
        val user = (application as FlowpayApp).loggedInuser
        if (user != null) {
            walletBalanceView.text = "₱${String.format("%,.2f", user.walletBalance)}"
        }

        // Load products for this category and wishlist from backend
        lifecycleScope.launch {
            val productsResponse = RetrofitInstance.api.getProductsByCategory(categoryId)
            val wishlistResponse = RetrofitInstance.api.getWishlist(currentUserId)
            if (productsResponse.isSuccessful && wishlistResponse.isSuccessful) {
                val products = productsResponse.body() ?: emptyList()
                val wishlist = wishlistResponse.body() ?: emptyList()
                categoryProducts = products
                wishlistedProducts.clear()
                wishlistedProducts.addAll(wishlist.map { it.product_id })
                updateAdapter(categoryProducts)
            }
        }

        // Set category title and background
        categoryTitleView.text = categoryName
        setCategoryBackgroundImage(categoryName)

        // Set up click listeners
        walletIcon.setOnClickListener {
            startActivity(Intent(this, WalletActivity::class.java))
        }
        mailIcon.setOnClickListener {
            startActivityForResult(Intent(this, MailsActivity::class.java), MAIL_LIST_REQUEST_CODE)
        }
        backButton.setOnClickListener {
            finish()
        }
        buyButton.setOnClickListener {
            purchaseSelectedProduct()
        }
    }

    private fun updateAdapter(products: List<Product>) {
        recyclerView.adapter = ProductAdapter(
            products,
            onProductSelected = { product -> onProductSelected(product) },
            onWishlistClicked = { product -> toggleWishlist(product) },
            isInWishlist = { product -> wishlistedProducts.contains(product.product_id) }
        )
    }

    private fun onProductSelected(product: Product) {
        if (selectedProduct == product) {
            // Deselect: panel should disappear immediately
            selectedProduct = null
            hideConfirmationPanel()
            (recyclerView.adapter as? ProductAdapter)?.setSelectedProduct(null)
        } else {
            // Select: show panel and update info
            selectedProduct = product
            showConfirmationPanel(product)
            (recyclerView.adapter as? ProductAdapter)?.setSelectedProduct(product)
        }
        updateConfirmationPanelVisibility()
    }

    private fun toggleWishlist(product: Product) {
        lifecycleScope.launch {
            if (wishlistedProducts.contains(product.product_id)) {
                val response = RetrofitInstance.api.removeFromWishlist(currentUserId, product.product_id)
                if (response.isSuccessful) {
                    wishlistedProducts.remove(product.product_id)
                    (recyclerView.adapter as? ProductAdapter)?.updateWishlistState(product)
                    Toast.makeText(this@ProductsListActivity, "Removed from wishlist", Toast.LENGTH_SHORT).show()
                }
            } else {
                val wishlist = Wishlist(userid = currentUserId, productid = product.product_id)
                val response = RetrofitInstance.api.addToWishlist(wishlist)
                if (response.isSuccessful) {
                    wishlistedProducts.add(product.product_id)
                    (recyclerView.adapter as? ProductAdapter)?.updateWishlistState(product)
                    Toast.makeText(this@ProductsListActivity, "Added to wishlist", Toast.LENGTH_SHORT).show()
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
                        confirmationPanel.visibility = View.GONE
                        Toast.makeText(this@ProductsListActivity, "Purchase successful!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@ProductsListActivity, "Error processing purchase", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationPanel(product: Product) {
        confirmationPanel.visibility = View.VISIBLE
        findViewById<TextView>(R.id.tv_selected_product).text = product.productName
        findViewById<TextView>(R.id.tv_selected_price).text = "₱${product.price}"
        findViewById<TextView>(R.id.tv_error_message).visibility = View.GONE
        val user = (application as FlowpayApp).loggedInuser
        val buyBtn = findViewById<Button>(R.id.btn_buy)
        if (user != null && user.walletBalance >= product.price) {
            buyBtn.isEnabled = true
            buyBtn.alpha = 1.0f
        } else {
            buyBtn.isEnabled = false
            buyBtn.alpha = 0.5f
            findViewById<TextView>(R.id.tv_error_message).visibility = View.VISIBLE
        }
        buyBtn.setOnClickListener {
            // Place purchase logic here
            purchaseSelectedProduct()
        }
    }

    private fun hideConfirmationPanel() {
        confirmationPanel.visibility = View.GONE
    }

    private fun updateConfirmationPanelVisibility() {
        if (selectedProduct == null) {
            hideConfirmationPanel()
        } else {
            showConfirmationPanel(selectedProduct!!)
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

    /**
     * Set the background image for the category
     * The image resource follows the pattern: img_[category]_bg
     * Category name is converted to lowercase and spaces are replaced with underscores
     */
    private fun setCategoryBackgroundImage(categoryName: String) {
        // Format the category name to match resource naming conventions
        val formattedCategoryName = categoryName.lowercase().replace(" ", "_")
        
        // Map of common game names to their abbreviations as they appear in file names
        val abbreviationMap = mapOf(
            "mobile_legends" to "ml",
            "league_of_legends" to "lol",
            "valorant" to "valo",
            "call_of_duty" to "cod",
            "wild_rift" to "wildrift",
            "pubg_mobile" to "pubg",
            "genshin_impact" to "genshin",
            "zenless_zone_zero" to "zenless",
            "honkai_star_rail" to "star_rail"
        )
        
        // Check if we have an abbreviation for this category
        val categoryCode = abbreviationMap[formattedCategoryName] ?: formattedCategoryName
        
        // Create the drawable resource name - using the existing pattern observed in drawable folder
        val backgroundResourceName = "img_${categoryCode}_bg"
        
        // Get the resource ID
        val resourceId = resources.getIdentifier(
            backgroundResourceName,
            "drawable",
            packageName
        )
        
        // Set the background image if resource exists, otherwise use a default background
        if (resourceId != 0) {
            categoryBackgroundView.setImageResource(resourceId)
        } else {
            // Try with the original formatted name as a fallback
            if (categoryCode != formattedCategoryName) {
                val fallbackResourceName = "img_${formattedCategoryName}_bg"
                val fallbackResourceId = resources.getIdentifier(
                    fallbackResourceName,
                    "drawable",
                    packageName
                )
                
                if (fallbackResourceId != 0) {
                    categoryBackgroundView.setImageResource(fallbackResourceId)
                    return
                }
            }
            
            // Use a default background image if the specific one doesn't exist
            // We'll use img_notfound.png as our fallback since it already exists
            categoryBackgroundView.setImageResource(R.drawable.img_notfound)
            
            // Log a message about the missing resource
            android.util.Log.d("ProductsListActivity", "Background image not found: $backgroundResourceName")
        }
    }
}
