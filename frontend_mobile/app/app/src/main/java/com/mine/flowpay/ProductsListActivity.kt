package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.adapters.ProductAdapter
import com.mine.flowpay.data.Product
import com.mine.flowpay.data.Transaction
import com.mine.flowpay.data.Wishlist
import com.mine.flowpay.viewmodel.ProductViewModel
import com.mine.flowpay.viewmodel.TransactionViewModel
import com.mine.flowpay.viewmodel.UserViewModel
import com.mine.flowpay.viewmodel.WishlistViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch

class ProductsListActivity : AppCompatActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val wishlistViewModel: WishlistViewModel by viewModels()
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryTitleView: TextView
    private lateinit var confirmationPanel: CardView
    private lateinit var selectedProductView: TextView
    private lateinit var selectedPriceView: TextView
    private lateinit var buyButton: Button
    private lateinit var errorText: TextView
    private lateinit var balanceView: TextView
    private var currentBalance: Double = 0.0
    private var selectedProduct: Product? = null
    private var currentUserId: Long = -1
    private var wishlistedProducts: MutableSet<Long> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)

        // Get user ID from intent
        currentUserId = intent.getLongExtra("USER_ID", -1)
        
        // Set up back press handling
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        // Get category details from intent
        val categoryId = intent.getLongExtra("CATEGORY_ID", -1)
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Products"

        // Initialize views
        initializeViews()
        setupClickListeners()
        
        // Set category title
        categoryTitleView.text = categoryName

        // Initially hide confirmation panel
        confirmationPanel.visibility = View.GONE

        // Set up RecyclerView with 2 columns
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        
        // Get products for this category and sort by price
        productViewModel.getProductsByCategory(categoryId)
        
        // Get current user
        userViewModel.getUserById(currentUserId)
        
        // Observe products
        productViewModel.allProducts.observe(this) { products ->
            val sortedProducts = products.sortedBy { it.price }
            recyclerView.adapter = ProductAdapter(sortedProducts) { product ->
                onProductSelected(product)
            }
            // Ensure all products are displayed
            recyclerView.adapter?.notifyDataSetChanged()
        }

        // Observe user balance
        userViewModel.currentUser.observe(this) { user ->
            user?.let {
                currentBalance = it.walletBalance
                updateBalanceDisplay()
            }
        }
    }

    private fun initializeViews() {
        categoryTitleView = findViewById(R.id.tv_category_title)
        recyclerView = findViewById(R.id.rv_products)
        confirmationPanel = findViewById(R.id.confirmation_panel)
        selectedProductView = findViewById(R.id.tv_selected_product)
        selectedPriceView = findViewById(R.id.tv_selected_price)
        buyButton = findViewById(R.id.btn_buy)
        errorText = findViewById(R.id.tv_error)
        balanceView = findViewById(R.id.tv_wallet_balance)
        
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupClickListeners() {
        balanceView.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        buyButton.setOnClickListener {
            selectedProduct?.let { product ->
                if (currentBalance >= product.price) {
                    // Process purchase
                    val newBalance = currentBalance - product.price
                    
                    // Create transaction first
                    val transaction = Transaction(
                        userId = currentUserId,
                        amount = product.price,
                        type = "PURCHASE"
                    )
                    
                    // Update balance and create transaction
                    viewModelScope.launch {
                        try {
                            // Create transaction first
                            transactionViewModel.createTransaction(transaction)
                            
                            // Update balance
                            userViewModel.updateWalletBalance(currentUserId, newBalance)
                            
                            // Observe the result
                            userViewModel.walletUpdateResult.observe(this@ProductsListActivity) { success ->
                                if (success) {
                                    Toast.makeText(this@ProductsListActivity, "Purchase successful!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@ProductsListActivity, "Error updating balance", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@ProductsListActivity, "Error processing purchase: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    showInsufficientBalanceError()
                }
            }
        }
    }

    private fun onProductSelected(product: Product) {
        selectedProduct = product
        confirmationPanel.visibility = View.VISIBLE
        
        selectedProductView.text = "${product.productName} • E-Wallet"
        selectedPriceView.text = formatPrice(product.price)
        
        // Check if user has sufficient balance
        if (currentBalance < product.price) {
            showInsufficientBalanceError()
        } else {
            errorText.visibility = View.GONE
            buyButton.isEnabled = true
        }
    }

    private fun showInsufficientBalanceError() {
        errorText.apply {
            visibility = View.VISIBLE
            text = "Insufficient balance. Please top up your wallet."
        }
        buyButton.isEnabled = false
    }

    private fun updateBalanceDisplay() {
        balanceView.text = formatPrice(currentBalance)
    }

    private fun formatPrice(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("en", "PH")).format(amount)
    }

    private fun toggleWishlist(product: Product) {
        if (wishlistedProducts.contains(product.product_id)) {
            // Remove from wishlist in database
            wishlistViewModel.removeFromWishlist(currentUserId, product.product_id)
            wishlistedProducts.remove(product.product_id)
        } else {
            // Add to wishlist in database
            val wishlist = Wishlist(
                user_id = currentUserId,
                product_id = product.product_id
            )
            wishlistViewModel.addToWishlist(wishlist)
            wishlistedProducts.add(product.product_id)
        }

        // Update the adapter to reflect changes
        updateAdapter()
    }
} 