package com.dbd.market.screens.fragments.market

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.dbd.market.R
import com.dbd.market.adapters.product_description.ProductDescriptionImageViewPager2Adapter
import com.dbd.market.adapters.product_description.ProductDescriptionSizesAdapter
import com.dbd.market.data.CartProduct
import com.dbd.market.data.Product
import com.dbd.market.databinding.FragmentProductDescriptionBinding
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.MIN_AMOUNT_OF_IMAGES_TO_SHOW_RECT_VIEW_PAGER2_INDICATOR
import com.dbd.market.viewmodels.market.ProductDescriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.vejei.viewpagerindicator.indicator.RectIndicator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDescriptionFragment : Fragment() {

    private lateinit var binding: FragmentProductDescriptionBinding
    private val productDescriptionArgs: ProductDescriptionFragmentArgs by navArgs()
    private val productDescriptionViewModel by viewModels<ProductDescriptionViewModel>()
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
        onCloseImageViewClick()
        addProductToCart(productArgs)
        observeProductDescriptionState()
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
            productDescriptionViewModel.changeProductDescriptionSelectedSizeValue(size)
//            selectedSize = size
        }
    }

    private fun onCloseImageViewClick() { binding.closeProductDescriptionScreen.setOnClickListener { requireActivity().onBackPressed() } }

    private fun addProductToCart(product: Product) {
        binding.productDescriptionAddToCartButton.setOnClickListener {
            if (selectedSize.isNotEmpty()) {
                val cartProduct = CartProduct(product.id, product.name, product.category, product.description, product.price, product.discount, selectedSize, product.images, 1)
                productDescriptionViewModel.addProductToCart(cartProduct)
            } else return@setOnClickListener
        }
    }

    private fun observeProductDescriptionState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    productDescriptionViewModel.productDescriptionSelectedSize.collect { size ->
                        when {
                            size.isNotEmpty() -> {
                                selectedSize = size
                                binding.productDescriptionSizesWarningTextView.visibility = View.GONE
                            }
                            size.isEmpty() -> binding.productDescriptionSizesWarningTextView.visibility = View.VISIBLE
                        }
                    }
                }
                launch {
                    productDescriptionViewModel.productDescriptionProductAddedToCart.collect {
                        when (it) {
                            is Resource.Success -> {
                                binding.productDescriptionAddToCartButton.revertAnimation()
                                showToast(requireContext(), binding.root, R.drawable.ic_done_icon, "You have successfully added this product to the cart")
                            }
                            is Resource.Loading -> binding.productDescriptionAddToCartButton.startAnimation()
                            is Resource.Error -> {
                                binding.productDescriptionAddToCartButton.revertAnimation()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.productDescriptionAddToCartButton.dispose()
    }

}