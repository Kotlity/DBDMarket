package com.dbd.market.adapters.setup_order

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class SetupOrderCartProductsAdapter(private val setupOrderCartProductsAdapterContext: Context): RecyclerView.Adapter<SetupOrderCartProductsAdapter.SetupOrderCartProductsViewHolder>() {

    inner class SetupOrderCartProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val setupOrderCartProductsViewPager2 = itemView.findViewById<ViewPager2>(R.id.setupOrderCartProductsViewPager2)
        private val setupOrderCartProductsViewPager2Indicator = itemView.findViewById<CircleIndicator>(R.id.setupOrderCartProductsViewPager2Indicator)
        private val setupOrderCartProductTitleTextView = itemView.findViewById<TextView>(R.id.setupOrderCartProductTitleTextView)
        private val setupOrderCartProductNewPriceTextView = itemView.findViewById<TextView>(R.id.setupOrderCartProductNewPriceTextView)
        private val setupOrderCartProductOldPriceTextView = itemView.findViewById<TextView>(R.id.setupOrderCartProductOldPriceTextView)
        private val setupOrderCartProductQuantityTextView = itemView.findViewById<TextView>(R.id.setupOrderCartProductQuantityTextView)

        fun bind(cartProduct: CartProduct) {
            setupOrderCartProductsChildAdapter(setupOrderCartProductsViewPager2, cartProduct.images)

            if (cartProduct.images.size >= Constants.MIN_AMOUNT_OF_IMAGES_TO_SHOW_VIEW_PAGER2_INDICATOR) {
                setupOrderCartProductsViewPager2Indicator.apply {
                    setWithViewPager2(setupOrderCartProductsViewPager2)
                    itemCount = cartProduct.images.size
                    setAnimationMode(CircleIndicator.AnimationMode.SLIDE)
                }
            } else setupOrderCartProductsViewPager2Indicator.visibility = View.GONE

            setupOrderCartProductTitleTextView.text = cartProduct.name
            if (cartProduct.discount != null) {
                setupOrderCartProductNewPriceTextView.text = getNewPriceAfterDiscount(cartProduct.price, cartProduct.discount)
                setupOrderCartProductOldPriceTextView.apply {
                    text = cartProduct.price.toString().plus("$")
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(setupOrderCartProductsAdapterContext, R.color.grey))
                }
            } else {
                setupOrderCartProductNewPriceTextView.visibility = View.GONE
                setupOrderCartProductOldPriceTextView.text = cartProduct.price.toString().plus("$")
            }
            setupOrderCartProductQuantityTextView.text = cartProduct.amount.toString()
        }
    }

    private fun setupOrderCartProductsChildAdapter(childViewPager2: ViewPager2, listOfImages: List<String>) {
        val setupOrderChildAdapter = ProductDescriptionImageViewPager2Adapter(setupOrderCartProductsAdapterContext, ViewPager2ImagesBackgroundType.WITHOUTSHADOW)
        childViewPager2.adapter = setupOrderChildAdapter
        setupOrderChildAdapter.differ.submitList(listOfImages)
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