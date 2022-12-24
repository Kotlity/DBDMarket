package com.dbd.market.screens.fragments.market.bottom_navigation

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.cart.CartAdapter
import com.dbd.market.data.CartProduct
import com.dbd.market.data.CartProductsSetupOrder
import com.dbd.market.databinding.FragmentCartBinding
import com.dbd.market.utils.*
import com.dbd.market.viewmodels.market.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartViewModel by activityViewModels<CartViewModel>()
    private lateinit var cartAdapter: CartAdapter
    private var cartProductsToSetupOrderFragment: CartProductsSetupOrder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartRecyclerView()
        increaseCartProductQuantity()
        decreaseCartProductQuantity()
        deleteCartProduct()
        closeCartFragment()
        navigateToSetupOrderFragment()
        observeCartProducts()
    }

    private fun setupCartRecyclerView() {
        cartAdapter = CartAdapter(requireContext())
        binding.cartProductsRecyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.CARTPRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInCartProductRecyclerView)))
        }
    }

    private fun increaseCartProductQuantity() {
        cartAdapter.onRecyclerViewPlusClick { cartProduct ->
            cartViewModel.increaseCartProductQuantity(cartProduct)
            observeCartProductPlusState()
        }
    }

    private fun decreaseCartProductQuantity() {
        cartAdapter.onRecyclerViewMinusClick { cartProduct ->
            if (cartProduct.amount == 1) showAlertDialogToDeleteCartProduct(cartProduct) else {
                cartViewModel.decreaseCartProductQuantity(cartProduct)
                observeCartProductMinusState()
            }
        }
    }

    private fun deleteCartProduct() {
        cartAdapter.onRecyclerViewDeleteClick { cartProduct ->
            showAlertDialogToDeleteCartProduct(cartProduct)
        }
    }

    private fun showAlertDialogToDeleteCartProduct(cartProduct: CartProduct) {
        val typedTitleFloatValue = TypedValue().also {
            requireContext().resources.getValue(R.dimen.customAlertDialogTitleTextViewSize, it, false)
        }.float
        val typedMessageFloatValue = TypedValue().also {
            requireContext().resources.getValue(R.dimen.customAlertDialogMessageTextViewSize, it, false)
        }.float

        showCustomAlertDialog(requireContext(),
            "Deleting ${cartProduct.name} from your cart",
            typedTitleFloatValue,
            "Are you sure you want to delete ${cartProduct.name} from your cart ?",
            typedMessageFloatValue,
            onPositiveButtonClick = {
                cartViewModel.deleteCartProduct(cartProduct)
                observeCartProductDeleteState()
            })
    }

    private fun calculateTotalPrice(cartProductsList: List<CartProduct>): Int {
        var totalPrice = 0
        cartProductsList.forEach { cartProduct ->
            totalPrice += if (cartProduct.discount != null) {
                val remainingPricePercentage = 1f - cartProduct.discount
                val newPriceAfterDiscount = String.format("%.0f", cartProduct.price * remainingPricePercentage).toInt()
                newPriceAfterDiscount * cartProduct.amount
            } else cartProduct.price * cartProduct.amount
        }
        binding.cartTotalPriceTextView.text = totalPrice.toString().plus("$")
        return totalPrice
    }

    private fun closeCartFragment() { binding.closeCartScreen.setOnClickListener { requireActivity().onBackPressed() } }

    private fun navigateToSetupOrderFragment() {
        binding.cartNextButton.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToSetupOrderFragment(cartProductsToSetupOrderFragment!!)
            findNavController().navigate(action)
        }
    }

    private fun observeCartProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProducts.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        hideCartFragmentProgressBar()
                        it.data?.let { cartProducts ->
                            if (cartProducts.isNotEmpty()) {
                                cartAdapter.differ.submitList(cartProducts)
                                val totalPrice = calculateTotalPrice(cartProducts)
                                cartProductsToSetupOrderFragment = CartProductsSetupOrder(cartProducts, totalPrice)
                            }
                            else {
                                cartAdapter.differ.submitList(cartProducts)
                                showEmptyCartProductWidgets()
                                hideWidgetsWhenCartProductsListIsEmpty()
                            }
                        }
                    }
                    is Resource.Loading -> showCartFragmentProgressBar()
                    is Resource.Error -> {
                        hideCartFragmentProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeCartProductPlusState() {
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProductPlus.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> hideCartFragmentProgressBar()
                    is Resource.Loading -> showCartFragmentProgressBar()
                    is Resource.Error -> {
                        hideCartFragmentProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeCartProductMinusState() {
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProductMinus.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> hideCartFragmentProgressBar()
                    is Resource.Loading -> showCartFragmentProgressBar()
                    is Resource.Error -> {
                        hideCartFragmentProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeCartProductDeleteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartProductDelete.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        hideCartFragmentProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_done_icon, getString(R.string.successfullyDeletedCartProductString))
                    }
                    is Resource.Loading -> showCartFragmentProgressBar()
                    is Resource.Error -> {
                        hideCartFragmentProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun hideCartFragmentProgressBar() {
        binding.cartFragmentProgressBar.visibility = View.GONE
    }

    private fun showCartFragmentProgressBar() {
        binding.cartFragmentProgressBar.visibility = View.VISIBLE
    }

    private fun showEmptyCartProductWidgets() {
        binding.emptyCartImageView.visibility = View.VISIBLE
        binding.emptyCartTextView.visibility = View.VISIBLE
    }

    private fun hideWidgetsWhenCartProductsListIsEmpty() {
        binding.apply {
            cartNextButton.visibility = View.GONE
            totalPriceConstraintLayout.visibility = View.GONE
        }
    }
}