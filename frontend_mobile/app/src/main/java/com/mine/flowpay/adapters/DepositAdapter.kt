package com.mine.flowpay.adapters

import com.mine.flowpay.data.Deposit
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mine.flowpay.R
import java.text.SimpleDateFormat
import java.util.Locale

class DepositAdapter(private val deposits: List<Deposit>) : 
    RecyclerView.Adapter<DepositAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val paymentMethodIcon: ImageView = view.findViewById(R.id.iv_payment_method)
        val paymentMethodText: TextView = view.findViewById(R.id.tv_payment_method)
        val dateTimeText: TextView = view.findViewById(R.id.tv_date_time)
        val amountText: TextView = view.findViewById(R.id.tv_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deposit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deposit = deposits[position]
        
        // Set payment method icon based on type
        val iconResource = when (deposit.paymentOption) {
            "GCash" -> R.drawable.logo_gcash
            "Maya" -> R.drawable.logo_paymaya
            "Visa" -> R.drawable.logo_visa2
            "Mastercard" -> R.drawable.logo_mastercard
            else -> R.drawable.ic_payment_default
        }
        holder.paymentMethodIcon.setImageResource(iconResource)
        
        // Set payment method name
        holder.paymentMethodText.text = deposit.paymentOption
        
        // Format and set date/time
        val dateFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
        holder.dateTimeText.text = dateFormat.format(deposit.datetime)
        
        // Format and set amount
        holder.amountText.text = String.format("₱%,.2f", deposit.amount)
    }

    override fun getItemCount() = deposits.size
} 