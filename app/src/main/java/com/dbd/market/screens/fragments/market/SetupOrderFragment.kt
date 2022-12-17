package com.dbd.market.screens.fragments.market

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.adapters.setup_order.SetupOrderCartProductsAdapter
import com.dbd.market.databinding.FragmentSetupOrderBinding
import com.dbd.market.utils.*
import com.dbd.market.viewmodels.market.SetupOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetupOrderFragment : Fragment() {
    private lateinit var binding: FragmentSetupOrderBinding
    private val args by navArgs<SetupOrderFragmentArgs>()
    private val setupOrderViewModel by viewModels<SetupOrderViewModel>()
    private lateinit var setupOrderCartProductsAdapter: SetupOrderCartProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetupOrderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrderCartProductsRecyclerView()
        setTotalPriceTextView()
        closeSetupOrderFragment()
        showCustomBottomSheetDialog()
        observeSetupOrderCartProductsState()
    }

    private fun setupOrderCartProductsRecyclerView() {
        setupOrderCartProductsAdapter = SetupOrderCartProductsAdapter(requireContext())
        binding.setupOrderCartProductsRecyclerView.apply {
            adapter = setupOrderCartProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.CARTPRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun setTotalPriceTextView() { binding.setupOrderTotalPriceTextView.text = args.cartProductsSetupOrder.totalPrice.toString().plus("$") }

    private fun closeSetupOrderFragment() { binding.closeSetupOrder.setOnClickListener { requireActivity().onBackPressed() } }

    private fun showCustomBottomSheetDialog() {
        binding.chooseAddressImageView.setOnClickListener {
            showBottomSheetDialog(requireContext(), onSuccess = {
                Log.d("MyTag", "first name: ${it.firstName}, last name: ${it.lastName}, phone number: ${it.phoneNumber}")
            })
        }
    }

    private fun observeSetupOrderCartProductsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            setupOrderViewModel.setupOrderCartProducts.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        hideSetupOrderProgressBar()
                        setupOrderCartProductsAdapter.differ.submitList(args.cartProductsSetupOrder.cartProductList)
                    }
                    is Resource.Loading -> showSetupOrderProgressBar()
                    is Resource.Error -> {
                        hideSetupOrderProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun showSetupOrderProgressBar() { binding.setupOrderProgressBar.visibility = View.VISIBLE }

    private fun hideSetupOrderProgressBar() { binding.setupOrderProgressBar.visibility = View.GONE }
}