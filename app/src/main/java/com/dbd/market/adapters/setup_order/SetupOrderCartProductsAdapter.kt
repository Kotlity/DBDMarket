package com.dbd.market.adapters.setup_order

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbd.market.R
import com.dbd.market.data.CartProduct
import com.dbd.market.utils.getNewPriceAfterDiscount

class SetupOrderCartProductsAdapter(private val thisContext: Context): RecyclerView.Adapter<SetupOrderCartProductsAdapter.SetupOrderCartProductsViewHolder>() {

    inner class SetupOrderCartProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val setupOrderCartProductImageView = itemView.findViewById<ImageView>(R.id.setupOrderCartProductImageView)
        private val setupOrderCartProductTitleTextView = itemView.findViewById<TextView>(R.id.setupOrderCartProductTitleTextView)
        private val setupOrderCartProductNewPriceTextView = itemView.findViewById<TextView>(R.id.setupOrderCartProductNewPriceTextView)
        private val setupOrderCartProductOldPriceTextView = itemView.findViewById<TextView>(R.id.setupOrderCartProductOldPriceTextView)
        private val setupOrderCartProductQuantityTextView = itemView.findViewById<TextView>(R.id.setupOrderCartProductQuantityTextView)

        fun bind(cartProduct: CartProduct) {
            Glide.with(itemView.context).load(cartProduct.images[0]).error(R.drawable.ic_error_icon).into(setupOrderCartProductImageView)
            setupOrderCartProductTitleTextView.text = cartProduct.name
            if (cartProduct.discount != null) {
                setupOrderCartProductNewPriceTextView.text = getNewPriceAfterDiscount(cartProduct.price, cartProduct.discount)
                setupOrderCartProductOldPriceTextView.apply {
                    text = cartProduct.price.toString().plus("$")
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(thisContext, R.color.grey))
                }
            } else {
                setupOrderCartProductNewPriceTextView.visibility = View.GONE
                setupOrderCartProductOldPriceTextView.text = cartProduct.price.toString().plus("$")
            }
            setupOrderCartProductQuantityTextView.text = cartProduct.amount.toString()
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<CartProduct>() {

        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetupOrderCartProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.setup_order_cart_products_recycler_view_item_layout, parent, false)
        return SetupOrderCartProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetupOrderCartProductsViewHolder, position: Int) {
        val currentSetupOrderCartProduct = differ.currentList[position]
        holder.bind(currentSetupOrderCartProduct)
    }

    override fun getItemCount() = differ.currentList.size
}