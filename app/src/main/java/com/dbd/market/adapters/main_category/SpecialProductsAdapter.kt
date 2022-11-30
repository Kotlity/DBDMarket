package com.dbd.market.adapters.main_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.bumptech.glide.Glide
import com.dbd.market.R
import com.dbd.market.data.Product
import com.dbd.market.utils.OnRecyclerViewItemClickInterface

class SpecialProductsAdapter: RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>(), OnRecyclerViewItemClickInterface {

    inner class SpecialProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val addToCartButton = itemView.findViewById<CircularProgressButton>(R.id.specialItemAddToCartButton)

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
        holder.addToCartButton.setOnClickListener { onItemClick?.let { it(currentProduct) } }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClick: ((Product) -> Unit)? = null

    override fun onRecyclerViewItemClick(onClick: (T: Any) -> Unit) { onItemClick = onClick }

}