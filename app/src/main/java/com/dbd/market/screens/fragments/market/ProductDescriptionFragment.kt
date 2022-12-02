package com.dbd.market.screens.fragments.market

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
import com.dbd.market.R
import com.dbd.market.adapters.product_description.ProductDescriptionImageViewPager2Adapter
import com.dbd.market.adapters.product_description.ProductDescriptionSizesAdapter
import com.dbd.market.data.Product
import com.dbd.market.databinding.FragmentProductDescriptionBinding
import com.dbd.market.utils.Constants
import com.dbd.market.utils.Constants.MIN_AMOUNT_OF_IMAGES_TO_SHOW_RECT_VIEW_PAGER2_INDICATOR
import com.dbd.market.utils.MarginItemDecoration
import com.dbd.market.utils.getNewPriceAfterDiscount
import io.github.vejei.viewpagerindicator.indicator.RectIndicator

class ProductDescriptionFragment : Fragment() {

    private lateinit var binding: FragmentProductDescriptionBinding
    private val productDescriptionArgs: ProductDescriptionFragmentArgs by navArgs()
    private lateinit var productDescriptionImageViewPager2Adapter: ProductDescriptionImageViewPager2Adapter
    private lateinit var productDescriptionSizesAdapter: ProductDescriptionSizesAdapter
    private var selectedSize = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDescriptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productArgs = productDescriptionArgs.categoriesProduct
        bindDataFromNavArgsToTextViews(productArgs)
        setupProductDescriptionImageViewPager2Adapter(productArgs)
        setupProductDescriptionSizesAdapter(productArgs)
        getSizeFromClick()
        handleSizesWarningTextView()
        onCloseImageViewClick()
    }

    private fun bindDataFromNavArgsToTextViews(product: Product) {
        binding.apply {
            productDescriptionTitleTextView.text = product.name
            productDescriptionCategoryTextView.text = product.category
            productDescriptionTextView.text = product.description
            product.discount?.let { discountValue ->
                productDescriptionNewPriceTextView.text = getNewPriceAfterDiscount(product.price, discountValue)
                productDescriptionOldPriceTextView.apply {
                    text = product.price.toString().plus("$")
                    setTextColor(ResourcesCompat.getColor(resources, R.color.grey, null))
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
            productDescriptionOldPriceTextView.text = product.price.toString().plus("$")
        }
    }

    private fun setupProductDescriptionImageViewPager2Adapter(product: Product) {
        productDescriptionImageViewPager2Adapter = ProductDescriptionImageViewPager2Adapter()
        binding.apply {
            productDescriptionViewPager2.adapter = productDescriptionImageViewPager2Adapter
            productDescriptionImageViewPager2Adapter.differ.submitList(product.images)
            if (product.images.size >= MIN_AMOUNT_OF_IMAGES_TO_SHOW_RECT_VIEW_PAGER2_INDICATOR) {
                rectViewPager2Indicator.apply {
                    setWithViewPager2(binding.productDescriptionViewPager2)
                    itemCount = product.images.size
                    setAnimationMode(RectIndicator.AnimationMode.SLIDE)
                }
            } else rectViewPager2Indicator.visibility = View.GONE
        }
    }

    private fun setupProductDescriptionSizesAdapter(product: Product) {
        productDescriptionSizesAdapter = ProductDescriptionSizesAdapter()
        binding.productDescriptionSizesRecyclerView.apply {
            adapter = productDescriptionSizesAdapter
            addItemDecoration(MarginItemDecoration(Constants.MARGIN_ITEM_DECORATION_SIZE, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachSizeInSizesRecyclerView)))
        }
        productDescriptionSizesAdapter.differ.submitList(product.size)
    }

    private fun getSizeFromClick() {
        productDescriptionSizesAdapter.onRecyclerViewItemClick {
            val size = it as String
            selectedSize = size
        }
    }

    private fun handleSizesWarningTextView() {
        binding.apply {
            productDescriptionAddToCartButton.setOnClickListener {
                if (selectedSize.isEmpty()) productDescriptionSizesWarningTextView.visibility = View.VISIBLE
                else productDescriptionSizesWarningTextView.visibility = View.GONE
            }
        }
    }

    private fun onCloseImageViewClick() { binding.closeProductDescriptionScreen.setOnClickListener { requireActivity().onBackPressed() } }

}