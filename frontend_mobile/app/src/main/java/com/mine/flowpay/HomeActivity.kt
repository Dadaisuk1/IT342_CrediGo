package com.mine.flowpay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.adapters.GameCategoryAdapter
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.MailsActivity
import com.mine.flowpay.viewmodel.MailViewModel
import android.view.View
import com.mine.flowpay.model.ProductCategory
import com.mine.flowpay.network.RetrofitInstance
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    companion object {
        private const val MAIL_LIST_REQUEST_CODE = 100
    }
    private var mailObserver: androidx.lifecycle.Observer<Int>? = null
    private lateinit var mailViewModel: MailViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var walletBalanceView: TextView
    private lateinit var walletIcon: ImageView
    private lateinit var mailIcon: ImageView

    private var currentUserId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Get user ID from FlowpayApp
        val app = application as FlowpayApp
        currentUserId = app.loggedInuser?.user_id ?: -1

        // Initialize ViewModels
        mailViewModel = ViewModelProvider(this).get(MailViewModel::class.java)

        // Initialize views
        recyclerView = findViewById(R.id.rv_game_categories)
        walletBalanceView = findViewById(R.id.tv_balance)
        walletIcon = findViewById(R.id.iv_wallet)
        mailIcon = findViewById(R.id.mail_icon)

        // Set wallet balance with commas and two decimal points
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

        // Set up mail observer
        mailObserver = androidx.lifecycle.Observer<Int> { count ->
            updateMailIcon(count, mailViewModel.hasPurchaseMail.value ?: false)
        }

        // Initial mail check
        mailViewModel.unreadMailCount.observe(this, mailObserver!!)

        // Observe purchase mail status
        mailViewModel.hasPurchaseMail.observe(this) { hasPurchaseMail ->
            updateMailIcon(mailViewModel.unreadMailCount.value ?: 0, hasPurchaseMail)
        }

        // Set up RecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns

        // Fetch categories from backend and set up adapter
        lifecycleScope.launch {
            val response = RetrofitInstance.api.getProductCategories()
            if (response.isSuccessful) {
                val categories = response.body() ?: emptyList()
                if (categories.isNotEmpty()) {
                    Log.d("HomeActivity", "Categories loaded: ${categories.size}")
                    categories.forEach { category ->
                        Log.d("HomeActivity", "Category: ${category.categoryname}, Image: ${category.image}")
                        try {
                            val resourceId = resources.getIdentifier(category.image, "drawable", packageName)
                            Log.d("HomeActivity", "Image resource ID: $resourceId")
                            if (resourceId != 0) {
                                Log.d("HomeActivity", "Image resource exists")
                            } else {
                                Log.e("HomeActivity", "Image resource does not exist: ${category.image}")
                            }
                        } catch (e: Exception) {
                            Log.e("HomeActivity", "Error finding resource: ${e.message}", e)
                        }
                    }
                    recyclerView.adapter = GameCategoryAdapter(categories)
                } else {
                    Log.e("HomeActivity", "No categories found")
                }
            } else {
                Log.e("HomeActivity", "Failed to load categories from backend")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAIL_LIST_REQUEST_CODE && resultCode == RESULT_OK) {
            // Force refresh mail count
            mailViewModel.updateUnreadMailCount()
        }
    }

    private fun updateMailIcon(unreadCount: Int, hasPurchaseMail: Boolean) {
        if (unreadCount > 0) {
            // If there are unread mails, use the unread icon
            mailIcon.setImageResource(R.drawable.ic_mail_unread)
        } else {
            // If there are no unread mails, use the regular icon
            mailIcon.setImageResource(R.drawable.ic_mail)
        }
    }

    override fun onResume() {
        super.onResume()
        // Remove and re-add observer to force refresh
        mailObserver?.let { observer ->
            mailViewModel.unreadMailCount.removeObserver(observer)
            mailViewModel.unreadMailCount.observe(this, observer)
        }

        // Force refresh purchase mail status
        mailViewModel.hasPurchaseMail.removeObservers(this)
        mailViewModel.hasPurchaseMail.observe(this) { hasPurchaseMail ->
            updateMailIcon(mailViewModel.unreadMailCount.value ?: 0, hasPurchaseMail)
        }
    }
}
