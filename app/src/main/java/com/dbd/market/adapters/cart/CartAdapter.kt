package com.dbd.market.adapters.cart

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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dbd.market.R
import com.dbd.market.data.CartProduct
import com.dbd.market.utils.Constants.CART_PRODUCT_IMAGE_VIEW_ANIMATION_DURATION
import com.dbd.market.utils.getNewPriceAfterDiscount

class CartAdapter(private val thisContext: Context): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val cartProductImageView = itemView.findViewById<ImageView>(R.id.cartProductImageView)
        private val cartProductTitleTextView = itemView.findViewById<TextView>(R.id.cartProductTitleTextView)
        private val cartProductNewPriceTextView = itemView.findViewById<TextView>(R.id.cartProductNewPriceTextView)
        private val cartProductOldPriceTextView = itemView.findViewById<TextView>(R.id.cartProductOldPriceTextView)
        private val cartProductSizeTextView = itemView.findViewById<TextView>(R.id.cartProductSizeTextView)
        private val cartProductQuantityTextView = itemView.findViewById<TextView>(R.id.cartProductQuantityTextView)
        val cartProductQuantityIncreaseImageView = itemView.findViewById<ImageView>(R.id.cartProductQuantityIncreaseImageView)
        val cartProductQuantityDecreaseImageView = itemView.findViewById<ImageView>(R.id.cartProductQuantityDecreaseImageView)
        val cartProductDeleteImageView = itemView.findViewById<ImageView>(R.id.cartProductDeleteImageView)

        fun bind(cartProduct: CartProduct) {
            Glide.with(itemView.context)
                .load(cartProduct.images[0])
                .error(R.drawable.ic_error_icon)
                .transition(DrawableTransitionOptions.withCrossFade(CART_PRODUCT_IMAGE_VIEW_ANIMATION_DURATION))
                .into(cartProductImageView)
            cartProductTitleTextView.text = cartProduct.name
            if (cartProduct.discount != null) {
                cartProductNewPriceTextView.text = getNewPriceAfterDiscount(cartProduct.price, cartProduct.discount)
                cartProductOldPriceTextView.apply {
                    text = cartProduct.price.toString().plus("$")
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(thisContext, R.color.grey))
                }
            } else {
                cartProductNewPriceTextView.visibility = View.GONE
                cartProductOldPriceTextView.text = cartProduct.price.toString().plus("$")
            }
            cartProductSizeTextView.text = cartProduct.size
            cartProductQuantityTextView.text = cartProduct.amount.toString()
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<CartProduct>() {

        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_products_recycler_view_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentCartProduct = differ.currentList[position]
        holder.apply {
            bind(currentCartProduct)
            cartProductQuantityIncreaseImageView.setOnClickListener { onPlusClickInternal?.let { it(currentCartProduct) } }
            cartProductQuantityDecreaseImageView.setOnClickListener { onMinusClickInternal?.let { it(currentCartProduct) } }
            cartProductDeleteImageView.setOnClickListener { onDeleteClickInternal?.let { it(currentCartProduct) } }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onPlusClickInternal: ((CartProduct) -> Unit)? = null

    private var onMinusClickInternal: ((CartProduct) -> Unit)? = null

    private var onDeleteClickInternal: ((CartProduct) -> Unit)? = null

    fun onRecyclerViewPlusClick(onPlusClick: (CartProduct) -> Unit) { onPlusClickInternal = onPlusClick }

    fun onRecyclerViewMinusClick(onMinusClick: (CartProduct) -> Unit) { onMinusClickInternal = onMinusClick }

    fun onRecyclerViewDeleteClick(onDeleteClick: (CartProduct) -> Unit) { onDeleteClickInternal = onDeleteClick }


}