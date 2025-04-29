package com.mine.flowpay.utils

import android.util.Log
import android.widget.Button
import com.mine.flowpay.data.Product
import com.mine.flowpay.data.Transaction
import com.mine.flowpay.data.repository.TransactionRepository

/**
 * Utility class to handle sort button selection and product sorting logic
 */
class SortButtonsHandler(
    private val btnPopular: Button,
    private val btnHighLow: Button,
    private val btnLowHigh: Button,
    private val btnAZ: Button,
    private val transactionRepository: TransactionRepository? = null,
    private val onSortChanged: (List<Product>) -> Unit
) {
    // Ensure button listeners are attached on creation
    init {
        setupButtonListeners()
    }

    private var currentProducts: List<Product> = emptyList()
    private var originalProducts: List<Product> = emptyList()
    private var sortType: SortType = SortType.MOST_RECENT
    private var isSearchActive: Boolean = false
    private var productPopularityMap: Map<String, Int> = emptyMap()
    
    // Enum to represent different sort types
    enum class SortType {
        MOST_RECENT, POPULAR, HIGH_LOW, LOW_HIGH, A_Z
    }
    
    /**
     * Set up click listeners for all sort buttons
     */
    private fun setupButtonListeners() {
        btnPopular.setOnClickListener {
            handleButtonClick(btnPopular, SortType.POPULAR)
        }
        
        btnHighLow.setOnClickListener {
            handleButtonClick(btnHighLow, SortType.HIGH_LOW)
        }
        
        btnLowHigh.setOnClickListener {
            handleButtonClick(btnLowHigh, SortType.LOW_HIGH)
        }
        
        btnAZ.setOnClickListener {
            handleButtonClick(btnAZ, SortType.A_Z)
        }
    }
    
    /**
     * Handle button clicks with toggle functionality
     */
    private fun handleButtonClick(button: Button, type: SortType) {
        if (button.isSelected) {
            // If already selected, deselect it and revert to MOST_RECENT
            button.isSelected = false
            sortType = SortType.MOST_RECENT
        } else {
            // Deselect all buttons
            deselectAllButtons()
            
            // Select the clicked button
            button.isSelected = true
            sortType = type
            
            // If Popular sort is selected, make sure we have up-to-date popularity data
            if (type == SortType.POPULAR) {
                updateProductPopularityCounts()
            }
        }
        
        // Apply the sorting
        applySorting()
    }
    
    /**
     * Deselect all buttons
     */
    private fun deselectAllButtons() {
        btnPopular.isSelected = false
        btnHighLow.isSelected = false
        btnLowHigh.isSelected = false
        btnAZ.isSelected = false
    }
    
    /**
     * Update the list of products and apply the current sort
     */
    fun updateProducts(products: List<Product>, isSearch: Boolean = false) {
        if (!isSearch) {
            // If this is a full product list update (not search),
            // store the original list order
            originalProducts = products
        }
        
        // Update current products - this is what we're sorting
        currentProducts = products
        isSearchActive = isSearch
        
        // If using POPULAR sort, update popularity counts
        if (sortType == SortType.POPULAR) {
            updateProductPopularityCounts()
        }
        
        // Apply sorting based on current sort type
        applySorting()
    }
    
    /**
     * Calculate popularity counts for all products based on transactions
     */
    private fun updateProductPopularityCounts() {
        transactionRepository?.let { repo ->
            try {
                // Get all transactions
                val allTransactions = repo.getAllTransactions()
                
                Log.d("SortButtonsHandler", "Fetched ${allTransactions.size} transactions for popularity count")
                
                // Count occurrences of each product in transactions
                val productCounts = mutableMapOf<String, Int>()
                
                // First, build a list of all current product names for matching
                val productNames = currentProducts.map { it.productName.lowercase().trim() }
                Log.d("SortButtonsHandler", "Current product names: $productNames")
                
                allTransactions.forEach { transaction ->
                    // Extract product name from transaction type (case insensitive comparison)
                    val transactionType = transaction.type.lowercase().trim()
                    
                    // Try to find a matching product name (exact match first)
                    var matchedProductName = currentProducts.find { 
                        it.productName.lowercase().trim() == transactionType 
                    }?.productName
                    
                    // If no exact match, try to find a product name that is contained in the transaction type
                    // This helps with formatting differences between transaction.type and product.productName
                    if (matchedProductName == null) {
                        matchedProductName = currentProducts.find { product ->
                            transactionType.contains(product.productName.lowercase().trim()) ||
                            product.productName.lowercase().trim().contains(transactionType)
                        }?.productName
                    }
                    
                    // If we found a match, increment the counter for this product
                    matchedProductName?.let { productName ->
                        productCounts[productName] = (productCounts[productName] ?: 0) + 1
                        Log.d("SortButtonsHandler", "Transaction matched to product: $productName, count: ${productCounts[productName]}")
                    } ?: run {
                        // If no match found, try to log just for debugging
                        Log.d("SortButtonsHandler", "No product match found for transaction type: ${transaction.type}")
                    }
                }
                
                productPopularityMap = productCounts
                
                Log.d("SortButtonsHandler", "Product popularity map: $productPopularityMap")
            } catch (e: Exception) {
                Log.e("SortButtonsHandler", "Error calculating popularity: ${e.message}", e)
            }
        } ?: run {
            Log.e("SortButtonsHandler", "Transaction repository is null - can't calculate popularity")
        }
    }
    
    /**
     * Apply sorting based on the current sort type
     */
    private fun applySorting() {
        val productsToSort = currentProducts
        
        val sortedProducts = when (sortType) {
            SortType.MOST_RECENT -> {
                // Return to original order (most recent)
                if (isSearchActive) {
                    // For search results, keep them in the original search order
                    productsToSort
                } else {
                    // For non-search, use the original product list order
                    // Make sure we don't return an empty list
                    if (originalProducts.isEmpty() && productsToSort.isNotEmpty()) {
                        productsToSort
                    } else {
                        originalProducts
                    }
                }
            }
            SortType.POPULAR -> {
                // Sort by popularity (number of sales)
                Log.d("SortButtonsHandler", "Applying POPULAR sort with ${productsToSort.size} products")
                
                productsToSort.sortedByDescending { product ->
                    // Look up product in popularity map, default to 0 if not found
                    val popularity = productPopularityMap[product.productName] ?: 0
                    Log.d("SortButtonsHandler", "Product '${product.productName}' has popularity score: $popularity")
                    popularity
                }.also {
                    Log.d("SortButtonsHandler", "Popular sort order: ${it.map { p -> p.productName }}")
                }
            }
            SortType.HIGH_LOW -> {
                // Sort by price from highest to lowest
                productsToSort.sortedByDescending { it.price }
            }
            SortType.LOW_HIGH -> {
                // Sort by price from lowest to highest
                productsToSort.sortedBy { it.price }
            }
            SortType.A_Z -> {
                // Sort alphabetically by product name
                productsToSort.sortedBy { it.productName }
            }
        }
        
        Log.d("SortButtonsHandler", "Final sorted product count: ${sortedProducts.size}")
        
        // Never return an empty list if we have products
        val finalProducts = if (sortedProducts.isEmpty() && productsToSort.isNotEmpty()) {
            Log.d("SortButtonsHandler", "Sorted products was empty, using original products instead")
            productsToSort
        } else {
            sortedProducts
        }
        
        // Notify caller with the sorted list
        onSortChanged(finalProducts)
    }
} 