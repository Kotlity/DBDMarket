package com.dbd.market.adapters.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.data.CartProduct

class CartAdapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<CartProduct>() {

        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout., parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = differ.currentList.size
}