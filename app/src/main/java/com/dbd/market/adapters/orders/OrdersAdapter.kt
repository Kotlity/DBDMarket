package com.dbd.market.adapters.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.data.Order
import java.text.SimpleDateFormat

class OrdersAdapter: RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val ordersIdTextView = itemView.findViewById<TextView>(R.id.ordersIdTextView)
        private val ordersDateTextView = itemView.findViewById<TextView>(R.id.ordersDateTextView)

        @SuppressLint("SimpleDateFormat")
        fun bind(order: Order) {
            ordersIdTextView.text = order.id.toString()
            val convertDateToString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.time)
            ordersDateTextView.text = convertDateToString
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Order>() {

        override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Order, newItem: Order) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_recycler_view_item_layout, parent, false)
        return OrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val currentOrder = differ.currentList[position]

        holder.bind(currentOrder)
    }

    override fun getItemCount() = differ.currentList.size

}