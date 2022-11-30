package com.dbd.market.adapters.product_description

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dbd.market.R
import com.dbd.market.utils.Constants.PRODUCT_DESCRIPTION_IMAGE_VIEW_ANIMATION_DURATION

class ProductDescriptionImageViewPager2Adapter: RecyclerView.Adapter<ProductDescriptionImageViewPager2Adapter.ProductDescriptionImageViewPager2ViewHolder>() {

    inner class ProductDescriptionImageViewPager2ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(image: String) {
            val imageView = itemView.findViewById<ImageView>(R.id.productDescriptionViewPager2ImageView)
            Glide.with(itemView).load(image).transition(DrawableTransitionOptions.withCrossFade(PRODUCT_DESCRIPTION_IMAGE_VIEW_ANIMATION_DURATION)).into(imageView)
        }
    }

    private val differCallBack = object: DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDescriptionImageViewPager2ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_description_view_pager2_item_layout, parent, false)
        return ProductDescriptionImageViewPager2ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductDescriptionImageViewPager2ViewHolder, position: Int) {
        val currentImage = differ.currentList[position]
        holder.bind(currentImage)
    }

    override fun getItemCount() = differ.currentList.size
}