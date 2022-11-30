package com.dbd.market.adapters.main_category

import android.graphics.Paint
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

class BeneficialProductsAdapter: RecyclerView.Adapter<BeneficialProductsAdapter.BeneficialProductsViewHolder>(), OnRecyclerViewItemClickInterface {

    inner class BeneficialProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val goToProductButton = itemView.findViewById<CircularProgressButton>(R.id.beneficialItemGoToProductButton)

        fun bind(product: Product) {
            val name = product.name
            val image = product.images[0]
            val priceBeforeDiscount = product.price
            val discountValue = product.discount
            val priceBeforeDiscountTextView = itemView.findViewById<TextView>(R.id.beneficialItemProductPriceBeforeDiscount)
            val priceAfterDiscountTextView = itemView.findViewById<TextView>(R.id.beneficialItemProductPriceAfterDiscount)
            discountValue?.let { discount ->
                val remainingPricePercentage = 1f - discountValue
                val priceAfterDiscountWithoutRounding = priceBeforeDiscount * remainingPricePercentage
                val priceAfterDiscountWithRounding = String.format("%.0f", priceAfterDiscountWithoutRounding)
                priceAfterDiscountTextView.text = priceAfterDiscountWithRounding.plus("$")
            }
            priceBeforeDiscountTextView.apply {
                text = priceBeforeDiscount.toString().plus("$")
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            itemView.findViewById<TextView>(R.id.beneficialItemProductNameTextView).text = name
            Glide.with(itemView).load(image).centerCrop().into(itemView.findViewById(R.id.beneficialProductImageView))
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficialProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.beneficial_product_recycler_view_layout, parent, false)
        return BeneficialProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BeneficialProductsViewHolder, position: Int) {
        val currentBeneficialProduct = differ.currentList[position]
        holder.bind(currentBeneficialProduct)
        holder.goToProductButton.setOnClickListener { onItemClick?.let { it(currentBeneficialProduct) } }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClick: ((Product) -> Unit)? = null

    override fun onRecyclerViewItemClick(onClick: (T: Any) -> Unit) { onItemClick = onClick }
}