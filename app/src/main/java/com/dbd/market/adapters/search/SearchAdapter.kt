package com.dbd.market.adapters.search

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dbd.market.R
import com.dbd.market.adapters.product_description.ProductDescriptionImageViewPager2Adapter
import com.dbd.market.data.Product
import com.dbd.market.utils.*
import io.github.vejei.viewpagerindicator.indicator.CircleIndicator
import io.github.vejei.viewpagerindicator.indicator.RectIndicator

class SearchAdapter(private val thisContext: Context): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val searchViewPager2Indicator = itemView.findViewById<RectIndicator>(R.id.searchViewPager2Indicator)
        private val searchViewPager2 = itemView.findViewById<ViewPager2>(R.id.searchViewPager2)
        private val searchCategoryTextView = itemView.findViewById<TextView>(R.id.searchCategoryTextView)
        private val searchNameTextView = itemView.findViewById<TextView>(R.id.searchNameTextView)
        private val searchNewPriceTextView = itemView.findViewById<TextView>(R.id.searchNewPriceTextView)
        private val searchOldPriceTextView = itemView.findViewById<TextView>(R.id.searchOldPriceTextView)
        private val spaceBetweenOldNewPricesTextViews = itemView.findViewById<View>(R.id.spaceBetweenOldNewPricesTextViews)
        private val searchSizesRecyclerView = itemView.findViewById<RecyclerView>(R.id.searchSizesRecyclerView)

        fun bind(product: Product) {
            setupChildSearchViewPager2Adapter(searchViewPager2, product.images)
            setupChildSearchSizesRecyclerViewAdapter(searchSizesRecyclerView, product.size)

            if (product.images.size >= Constants.MIN_AMOUNT_OF_IMAGES_TO_SHOW_VIEW_PAGER2_INDICATOR) {
                searchViewPager2Indicator.apply {
                    setWithViewPager2(searchViewPager2)
                    itemCount = product.images.size
                    setAnimationMode(RectIndicator.AnimationMode.SLIDE)
                }
            } else searchViewPager2Indicator.visibility = View.GONE

            searchCategoryTextView.text = formatString(product.category)
            searchNameTextView.text = product.name

            if (product.discount != null) {
                searchNewPriceTextView.text = getNewPriceAfterDiscount(product.price, product.discount)
                searchOldPriceTextView.apply {
                    text = product.price.toString().plus("$")
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(thisContext, R.color.grey))
                }
            } else {
                searchNewPriceTextView.visibility = View.GONE
                spaceBetweenOldNewPricesTextViews.visibility = View.GONE
                searchOldPriceTextView.text = product.price.toString().plus("$")
            }
        }
    }

    private fun setupChildSearchViewPager2Adapter(searchViewPager2: ViewPager2, images: List<String>) {
        val searchChildViewPager2Adapter = ProductDescriptionImageViewPager2Adapter(thisContext, ViewPager2ImagesBackgroundType.WITHSHADOW)
        searchViewPager2.adapter = searchChildViewPager2Adapter
        searchChildViewPager2Adapter.differ.submitList(images)
    }

    private fun setupChildSearchSizesRecyclerViewAdapter(searchSizesRecyclerView: RecyclerView, sizes: List<String>) {
        val searchSizeAdapter = SearchSizeAdapter(thisContext)
        searchSizesRecyclerView.apply {
            adapter = searchSizeAdapter
            layoutManager = LinearLayoutManager(thisContext, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.SIZE, thisContext.resources.getDimensionPixelSize(R.dimen.spaceBetweenSearchSize)))
        }
        searchSizeAdapter.differ.submitList(sizes)
    }

    private val differCallback = object: DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_recycler_view_item_layout, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentSearchedProduct = differ.currentList[position]
        holder.bind(currentSearchedProduct)
    }

    override fun getItemCount() = differ.currentList.size

}