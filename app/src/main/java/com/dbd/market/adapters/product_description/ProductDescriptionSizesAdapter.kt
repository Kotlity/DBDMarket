package com.dbd.market.adapters.product_description

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.utils.OnRecyclerViewItemClickInterface
import de.hdodenhof.circleimageview.CircleImageView

class ProductDescriptionSizesAdapter: RecyclerView.Adapter<ProductDescriptionSizesAdapter.ProductDescriptionSizesViewHolder>(), OnRecyclerViewItemClickInterface {

    inner class ProductDescriptionSizesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productDescriptionSizeTextView = itemView.findViewById<TextView>(R.id.productDescriptionSizeTextView)
        val productDescriptionSizeImageViewShadow = itemView.findViewById<CircleImageView>(R.id.productDescriptionSizeImageViewShadow)

        fun bind(size: String) { productDescriptionSizeTextView.text = size }
    }

    private val differCallback = object: DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDescriptionSizesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_description_sizes_item_layout, parent, false)
        return ProductDescriptionSizesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductDescriptionSizesViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)

        if (selectedSizePosition == position) holder.productDescriptionSizeImageViewShadow.visibility = View.VISIBLE
        else holder.productDescriptionSizeImageViewShadow.visibility = View.INVISIBLE

        holder.itemView.setOnClickListener {
            if(selectedSizePosition >= 0) notifyItemChanged(selectedSizePosition)
            selectedSizePosition = holder.adapterPosition
            notifyItemChanged(selectedSizePosition)
            clickOnSize?.let { it(currentItem) }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var selectedSizePosition = -1

    private var clickOnSize: ((String) -> Unit)? = null

    override fun onRecyclerViewItemClick(onClick: (T: Any) -> Unit) { clickOnSize = onClick }
}