package com.mine.flowpay.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.R
import com.mine.flowpay.data.Transaction
import com.mine.flowpay.app.FlowpayApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(
    private val transactions: List<Transaction>,
    private val onTransactionClicked: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productCategoryView: TextView = view.findViewById(R.id.tv_product_category)
        val productNameView: TextView = view.findViewById(R.id.tv_product_name)
        val priceView: TextView = view.findViewById(R.id.tv_price)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onTransactionClicked(transactions[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        
        // Parse the transaction type to extract product name and category if possible
        val parts = transaction.type.split(" - ")
        
        if (parts.size > 1) {
            // If the transaction type contains a separator, use that information
            holder.productNameView.text = parts[0]
            holder.productCategoryView.text = parts[1]
        } else {
            // If no separator, just use the type as product name and try to find the category
            holder.productNameView.text = transaction.type
            
            // Set a default category while we try to find the real one
            holder.productCategoryView.text = "Loading category..."
            
            // Try to get the app context to access ViewModels
            val context = holder.itemView.context
            if (context != null) {
                val app = context.applicationContext as? FlowpayApp
                if (app != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            // Get the product repository
                            val database = app.database
                            val productDao = database.productDao()
                            
                            // Find product by name
                            val product = productDao.getProductByName(transaction.type)
                            if (product != null) {
                                // Get category name
                                val categoryDao = database.categoryDao()
                                val category = categoryDao.getCategoryById(product.category_id)
                                
                                withContext(Dispatchers.Main) {
                                    // Update the category text
                                    holder.productCategoryView.text = category?.category_name ?: "Unknown"
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    holder.productCategoryView.text = "Unknown Category"
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                holder.productCategoryView.text = "Unknown Category"
                            }
                        }
                    }
                } else {
                    holder.productCategoryView.text = "Unknown Category"
                }
            } else {
                holder.productCategoryView.text = "Unknown Category"
            }
        }
        
        // Format price with 2 decimal places
        holder.priceView.text = "₱${String.format("%.2f", transaction.amount)}"
        holder.priceView.setTextColor(holder.priceView.context.getColor(R.color.green))
    }

    override fun getItemCount() = transactions.size
}