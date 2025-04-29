package com.mine.flowpay.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.R
import com.mine.flowpay.data.models.Product
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private var products: List<Product>,
    private val onProductClick: (Product) -> Unit,
    private val onWishlistClick: (Product) -> Unit,
    private val wishlistedProducts: Set<Long> = emptySet()
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.tv_product_name)
        private val priceView: TextView = itemView.findViewById(R.id.tv_product_price)
        private val wishlistIcon: ImageView = itemView.findViewById(R.id.iv_wishlist)

        fun bind(product: Product) {
            nameView.text = "${product.name} VP"
            priceView.text = formatPrice(product.price)
            
            // Set wishlist icon state
            wishlistIcon.setImageResource(
                if (wishlistedProducts.contains(product.id)) R.drawable.ic_heart_filled
                else R.drawable.ic_heart_outline
            )

            itemView.setOnClickListener { onProductClick(product) }
            wishlistIcon.setOnClickListener { onWishlistClick(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    fun updateWishlistedProducts(newWishlistedProducts: Set<Long>) {
        // Only notify items that changed
        products.forEachIndexed { index, product ->
            val wasWishlisted = wishlistedProducts.contains(product.id)
            val isWishlisted = newWishlistedProducts.contains(product.id)
            if (wasWishlisted != isWishlisted) {
                notifyItemChanged(index)
            }
        }
    }

    private fun formatPrice(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
        return format.format(price)
    }
} 