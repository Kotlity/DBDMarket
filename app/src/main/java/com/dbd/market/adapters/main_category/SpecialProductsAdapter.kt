package com.dbd.market.adapters.main_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbd.market.R
import com.dbd.market.helpers.products_adder.data.Product

class SpecialProductsAdapter: RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            val name = product.name
            val price = product.price
            val image = product.images[0]

            itemView.findViewById<TextView>(R.id.specialItemProductNameTextView).text = name
            itemView.findViewById<TextView>(R.id.specialItemProductPriceTextView).text = price.toString().plus("$")
            Glide.with(itemView).load(image).into(itemView.findViewById(R.id.specialProductImageView))
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.speacial_product_recycler_view_layout, parent, false)
        return SpecialProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val currentProduct = differ.currentList[position]
        holder.bind(currentProduct)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}