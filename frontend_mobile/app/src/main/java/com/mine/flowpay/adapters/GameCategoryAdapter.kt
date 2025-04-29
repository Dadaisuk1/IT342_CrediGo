package com.mine.flowpay.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.ProductsListActivity
import com.mine.flowpay.R
import com.mine.flowpay.app.FlowpayApp
import com.mine.flowpay.data.ProductCategory

class GameCategoryAdapter(private val categories: List<ProductCategory>) : 
    RecyclerView.Adapter<GameCategoryAdapter.GameCategoryViewHolder>() {

    class GameCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_game_image)
        val titleView: TextView = view.findViewById(R.id.tv_game_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_category, parent, false)
        return GameCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameCategoryViewHolder, position: Int) {
        val category = categories[position]

        // Set category name
        holder.titleView.text = category.category_name
        holder.titleView.visibility = View.VISIBLE

        // Set image with proper scaling
        holder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        try {
            val resId = holder.imageView.context.resources.getIdentifier(category.image, "drawable", holder.imageView.context.packageName)
            if (resId != 0) {
                holder.imageView.setImageResource(resId)
            } else {
                holder.imageView.setImageResource(R.drawable.img_notfound)
            }
        } catch (e: Exception) {
            android.util.Log.e("GameCategoryAdapter", "Error loading image for ${category.category_name}: ${e.message}")
            holder.imageView.setImageResource(R.drawable.img_notfound)
        }

        // Set click listener
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val userId = (context.applicationContext as FlowpayApp).loggedInuser?.user_id ?: -1

            val intent = Intent(context, ProductsListActivity::class.java).apply {
                putExtra("CATEGORY_ID", category.category_id)
                putExtra("CATEGORY_NAME", category.category_name)
                putExtra("USER_ID", userId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = categories.size
}
