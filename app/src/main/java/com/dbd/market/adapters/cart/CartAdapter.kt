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
import androidx.viewpager2.widget.ViewPager2
import com.dbd.market.R
import com.dbd.market.adapters.product_description.ProductDescriptionImageViewPager2Adapter
import com.dbd.market.data.CartProduct
import com.dbd.market.utils.Constants
import com.dbd.market.utils.ViewPager2ImagesBackgroundType
import com.dbd.market.utils.getNewPriceAfterDiscount
import io.github.vejei.viewpagerindicator.indicator.CircleIndicator

class CartAdapter(private val thisContext: Context): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val cartProductsViewPager2 = itemView.findViewById<ViewPager2>(R.id.cartProductsViewPager2)
        private val cartProductsViewPager2Indicator = itemView.findViewById<CircleIndicator>(R.id.cartProductsViewPager2Indicator)
        private val cartProductTitleTextView = itemView.findViewById<TextView>(R.id.cartProductTitleTextView)
        private val cartProductNewPriceTextView = itemView.findViewById<TextView>(R.id.cartProductNewPriceTextView)
        private val cartProductOldPriceTextView = itemView.findViewById<TextView>(R.id.cartProductOldPriceTextView)
        private val cartProductSizeTextView = itemView.findViewById<TextView>(R.id.cartProductSizeTextView)
        private val cartProductQuantityTextView = itemView.findViewById<TextView>(R.id.cartProductQuantityTextView)
        val cartProductQuantityIncreaseImageView = itemView.findViewById<ImageView>(R.id.cartProductQuantityIncreaseImageView)
        val cartProductQuantityDecreaseImageView = itemView.findViewById<ImageView>(R.id.cartProductQuantityDecreaseImageView)
        val cartProductDeleteImageView = itemView.findViewById<ImageView>(R.id.cartProductDeleteImageView)

        fun bind(cartProduct: CartProduct) {
            setupCartChildAdapter(cartProductsViewPager2, cartProduct.images)

            if (cartProduct.images.size >= Constants.MIN_AMOUNT_OF_IMAGES_TO_SHOW_RECT_VIEW_PAGER2_INDICATOR) {
                cartProductsViewPager2Indicator.apply {
                    setWithViewPager2(cartProductsViewPager2)
                    itemCount = cartProduct.images.size
                    setAnimationMode(CircleIndicator.AnimationMode.SLIDE)
                }
            } else cartProductsViewPager2Indicator.visibility = View.GONE

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

    private fun setupCartChildAdapter(childViewPager2: ViewPager2, listOfImages: List<String>) {
        val cartChildAdapter = ProductDescriptionImageViewPager2Adapter(thisContext, ViewPager2ImagesBackgroundType.WITHOUTSHADOW)
        childViewPager2.adapter = cartChildAdapter
        cartChildAdapter.differ.submitList(listOfImages)
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