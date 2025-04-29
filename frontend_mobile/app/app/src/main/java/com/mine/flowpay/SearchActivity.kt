private fun performSearch(query: String) {
    if (query.isBlank()) {
        Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
        return
    }

    // Filter products based on search query
    val allProducts = productViewModel.allProducts.value ?: emptyList()
    searchResults.clear()
    
    // First try to find a category match
    val allCategories = categoryViewModel.allCategories
    val matchingCategory = allCategories.find { category ->
        category.category_name.contains(query, ignoreCase = true)
    }

    if (matchingCategory != null) {
        // If we found a matching category, show its products
        searchResults.addAll(allProducts.filter { product ->
            product.category_id == matchingCategory.category_id
        })
    } else {
        // If no matching category, search in product names
        searchResults.addAll(allProducts.filter { product ->
            product.productName.contains(query, ignoreCase = true)
        })
    }

    // Update adapter
    updateSearchResults()
}

private fun updateSearchResults() {
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
        Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show()
    }
} 