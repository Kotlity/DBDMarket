package com.dbd.market.screens.fragments.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.orders.OrdersAdapter
import com.dbd.market.databinding.FragmentOrdersBinding
import com.dbd.market.utils.*
import com.dbd.market.viewmodels.market.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private val ordersViewModel by viewModels<OrdersViewModel>()
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrdersRecyclerView()
        navigateToHomeFragment()
        handleOnBackPressedButton()
        observeOrdersState()
    }

    private fun setupOrdersRecyclerView() {
        ordersAdapter = OrdersAdapter()
        binding.ordersRecyclerView.apply {
            adapter = ordersAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.CARTPRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun navigateToHomeFragment() { navigateToAnotherFragmentWithoutArguments(binding.closeOrders, R.id.action_ordersFragment_to_homeFragment) }

    private fun handleOnBackPressedButton() { onBackButtonPressed(R.id.action_ordersFragment_to_homeFragment) }

    private fun observeOrdersState() {
        viewLifecycleOwner.lifecycleScope.launch {
            ordersViewModel.orders.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        hideOrdersProgressBar()
                        it.data?.let { sortedListOfOrders ->
                            if (sortedListOfOrders.isNotEmpty()) {
                                hideEmptyWidgets()
                                ordersAdapter.differ.submitList(sortedListOfOrders)
                            } else showEmptyWidgets()
                        }
                    }
                    is Resource.Loading -> showOrdersProgressBar()
                    is Resource.Error -> {
                        hideOrdersProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun showOrdersProgressBar() { binding.ordersProgressBar.visibility = View.VISIBLE }

    private fun hideOrdersProgressBar() { binding.ordersProgressBar.visibility = View.GONE }

    private fun showEmptyWidgets() {
        binding.apply {
            ordersEmptyImageView.visibility = View.VISIBLE
            ordersEmptyTextView.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyWidgets() {
        binding.apply {
            ordersEmptyImageView.visibility = View.GONE
            ordersEmptyTextView.visibility = View.GONE
        }
    }
}