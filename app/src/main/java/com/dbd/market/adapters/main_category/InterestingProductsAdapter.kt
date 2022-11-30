package com.dbd.market.adapters.main_category

import android.graphics.Paint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.bumptech.glide.Glide
import com.dbd.market.R
import com.dbd.market.data.Product
import com.dbd.market.utils.OnRecyclerViewItemClickInterface

class InterestingProductsAdapter: RecyclerView.Adapter<InterestingProductsAdapter.InterestingProductsViewHolder>(), OnRecyclerViewItemClickInterface {

    inner class InterestingProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val goToProductButton = itemView.findViewById<CircularProgressButton>(R.id.interestingItemGoToProductButton)

        fun bind(product: Product) {
            val name = product.name
            val image = product.images[0]
            val priceBeforeDiscount = product.price
            val discountValue = product.discount
            val priceBeforeDiscountTextView = itemView.findViewById<TextView>(R.id.interestingItemProductPriceBeforeDiscount)
            val priceAfterDiscountTextView = itemView.findViewById<TextView>(R.id.interestingItemProductPriceAfterDiscount)
            if (discountValue != null) {
                val remainingPricePercentage = 1f - discountValue
                val priceAfterDiscountWithoutRounding = priceBeforeDiscount * remainingPricePercentage
                val priceAfterDiscountWithRounding = String.format("%.0f", priceAfterDiscountWithoutRounding)
                priceAfterDiscountTextView.text = priceAfterDiscountWithRounding.plus("$")
                priceBeforeDiscountTextView.apply {
                    text = priceBeforeDiscount.toString().plus("$")
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.marginStart = 15
                priceAfterDiscountTextView.visibility = View.GONE
                priceBeforeDiscountTextView.apply {
                    text = priceBeforeDiscount.toString().plus("$")
                    gravity = Gravity.START
                    layoutParams = params
                    setTextColor(resources.getColor(R.color.black))
                }
            }
            itemView.findViewById<TextView>(R.id.interestingItemProductNameTextView).text = name
            Glide.with(itemView).load(image).into(itemView.findViewById(R.id.interestingProductImageView))
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestingProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.interesting_product_recycler_vew_layout, parent, false)
        return InterestingProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestingProductsViewHolder, position: Int) {
        val currentInterestingProduct = differ.currentList[position]
        holder.bind(currentInterestingProduct)

        holder.goToProductButton.setOnClickListener { onItemClick?.let { it(currentInterestingProduct) } }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClick: ((Product) -> Unit)? = null

    override fun onRecyclerViewItemClick(onClick: (T: Any) -> Unit) { onItemClick = onClick }
}