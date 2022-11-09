package com.dbd.market.helpers.products_adder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.helpers.products_adder.data.SelectedImage

class ProductsAdderAdapter: RecyclerView.Adapter<ProductsAdderAdapter.ProductsAdderViewHolder>() {

    inner class ProductsAdderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(selectedImage: SelectedImage) {
            val selectedImageIdTextView = itemView.findViewById<TextView>(R.id.selectedImageIdTextView)
            val selectedImageView = itemView.findViewById<ImageView>(R.id.selectedImageView)

            selectedImageIdTextView.text = selectedImage.id.plus(1).toString()
            selectedImageView.setImageURI(selectedImage.imageUri)
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<SelectedImage>() {

        override fun areItemsTheSame(oldItem: SelectedImage, newItem: SelectedImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SelectedImage, newItem: SelectedImage): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selected_images_from_gallery_recycler_view_item_layout, parent, false)
        return ProductsAdderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsAdderViewHolder, position: Int) {
        val selectedImage = differ.currentList[position]
        holder.bind(selectedImage)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}