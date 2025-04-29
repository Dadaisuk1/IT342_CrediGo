package com.mine.flowpay

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.adapters.DepositAdapter
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.data.repository.DepositRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel

class DepositHistoryActivity : AppCompatActivity() {
    private lateinit var depositRepository: DepositRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateView: View
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_history)

        // Initialize repository
        depositRepository = DepositRepository((application as FlowpayApp).database.depositDao())

        // Initialize views
        recyclerView = findViewById(R.id.rv_deposits)
        emptyStateView = findViewById(R.id.empty_state)
        
        // Set header title
        findViewById<TextView>(R.id.tv_header_title).text = "Deposit History"
        
        // Set up back button
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Load deposits
        loadDeposits()
    }

    private fun loadDeposits() {
        coroutineScope.launch {
            val deposits = depositRepository.getAllDeposits()
            if (deposits.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyStateView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyStateView.visibility = View.GONE
                recyclerView.adapter = DepositAdapter(deposits)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
} 