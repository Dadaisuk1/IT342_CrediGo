package com.mine.flowpay

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.adapters.GameCategoryAdapter
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.data.ProductCategory
import com.mine.flowpay.viewmodel.ProductCategoryViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: ProductCategoryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var walletBalanceView: TextView

    private var currentUserId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Get user ID from FlowpayApp
        val app = application as FlowpayApp
        currentUserId = app.loggedInuser?.user_id ?: -1

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ProductCategoryViewModel::class.java)

        // Initialize views
        recyclerView = findViewById(R.id.rv_game_categories)
        walletBalanceView = findViewById(R.id.tv_wallet_balance)

        // Set wallet balance
        walletBalanceView.text = "₱${app.loggedInuser?.walletBalance ?: 0.0}"

        // Set up RecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns

        // Observe categories changes
        viewModel.allCategories.observe(this) { categories ->
            Log.d("HomeActivity", "Categories loaded: ${categories.size}")
            categories.forEach { category ->
                Log.d("HomeActivity", "Category: ${category.category_name}, Image: ${category.image}")
            }
            recyclerView.adapter = GameCategoryAdapter(categories)
        }
    }
} 