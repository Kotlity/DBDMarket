package com.dbd.market.adapters.order_detail

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

class OrderDetailParentAdapter(private val orderDetailParentAdapterContext: Context): RecyclerView.Adapter<OrderDetailParentAdapter.OrderDetailParentViewHolder>() {

    inner class OrderDetailParentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val orderDetailImagesViewPager2 = itemView.findViewById<ViewPager2>(R.id.orderDetailImagesViewPager2)
        private val orderDetailImagesCircleViewPager2Indicator = itemView.findViewById<CircleIndicator>(R.id.orderDetailImagesCircleViewPager2Indicator)
        private val cartProductTitleTextView = itemView.findViewById<TextView>(R.id.cartProductTitleTextView)
        private val cartProductNewPriceTextView = itemView.findViewById<TextView>(R.id.cartProductNewPriceTextView)
        private val cartProductOldPriceTextView = itemView.findViewById<TextView>(R.id.cartProductOldPriceTextView)
        private val cartProductSizeTextView = itemView.findViewById<TextView>(R.id.cartProductSizeTextView)
        private val orderDetailAmountTextView = itemView.findViewById<TextView>(R.id.orderDetailAmountTextView)

        fun bind(cartProduct: CartProduct) {
            setupOrderDetailChildAdapter(orderDetailImagesViewPager2, cartProduct.images)

            if (cartProduct.images.size >= Constants.MIN_AMOUNT_OF_IMAGES_TO_SHOW_VIEW_PAGER2_INDICATOR) {
                orderDetailImagesCircleViewPager2Indicator.apply {
                    setWithViewPager2(orderDetailImagesViewPager2)
                    itemCount = cartProduct.images.size
                    setAnimationMode(CircleIndicator.AnimationMode.SLIDE)
                }
            } else orderDetailImagesCircleViewPager2Indicator.visibility = View.GONE

            cartProductTitleTextView.text = cartProduct.name
            if (cartProduct.discount != null) {
                cartProductNewPriceTextView.text = getNewPriceAfterDiscount(cartProduct.price, cartProduct.discount)
                cartProductOldPriceTextView.apply {
                    text = cartProduct.price.toString().plus("$")
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(orderDetailParentAdapterContext, R.color.grey))
                }
            } else {
                cartProductNewPriceTextView.visibility = View.GONE
                cartProductOldPriceTextView.text = cartProduct.price.toString().plus("$")
            }
            cartProductSizeTextView.text = cartProduct.size
            orderDetailAmountTextView.text = orderDetailAmountTextView.text.toString().plus("${cartProduct.amount}")
        }
    }

    private fun setupOrderDetailChildAdapter(childViewPager2: ViewPager2, listOfImages: List<String>) {
        val orderDetailChildAdapter = ProductDescriptionImageViewPager2Adapter(orderDetailParentAdapterContext, ViewPager2ImagesBackgroundType.WITHSHADOW)
        childViewPager2.adapter = orderDetailChildAdapter
        orderDetailChildAdapter.differ.submitList(listOfImages)
    }

    private val differCallback = object: DiffUtil.ItemCallback<CartProduct>() {

        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_detail_recycler_view_layout, parent, false)
        return OrderDetailParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderDetailParentViewHolder, position: Int) {
        val currentOrderDetailItem = differ.currentList[position]
        holder.bind(currentOrderDetailItem)
    }

    override fun getItemCount() = differ.currentList.size
}