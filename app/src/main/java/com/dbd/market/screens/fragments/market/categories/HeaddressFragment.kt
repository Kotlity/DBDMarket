package com.dbd.market.screens.fragments.market.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.adapters.main_category.InterestingProductsAdapter
import com.dbd.market.adapters.main_category.ProfitableCategoryProductsAdapter
import com.dbd.market.databinding.FragmentHeaddressBinding
import com.dbd.market.screens.fragments.market.HomeFragmentDirections
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD
import com.dbd.market.viewmodels.market.categories.headdress.HeaddressCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HeaddressFragment: BaseCategoryFragment<FragmentHeaddressBinding>(FragmentHeaddressBinding::inflate) {

    private val headdressCategoryViewModel by viewModels<HeaddressCategoryViewModel>()
    private lateinit var headdressProfitableAdapter: ProfitableCategoryProductsAdapter
    private lateinit var headdressOtherAdapter: InterestingProductsAdapter
    private lateinit var headdressProfitableLinearLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeaddressProfitableRecyclerView()
        setupHeaddressOtherRecyclerView()
        observeHeaddressCategoryState()
        headdressOtherProductsRecyclerViewReachedBottom()
        onHeaddressProfitableProductClick()
        onHeaddressOtherProductClick()
    }

    override fun onResume() {
        super.onResume()
        autoScrollHeaddressProfitableProductsRecyclerViewLogic()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        timerTask.cancel()
    }

    private fun setupHeaddressProfitableRecyclerView() {
        headdressProfitableAdapter = ProfitableCategoryProductsAdapter()
        headdressProfitableLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.headdressProfitableProductsRecyclerView.apply {
            adapter = headdressProfitableAdapter
            layoutManager = headdressProfitableLinearLayoutManager
        }
    }

    private fun autoScrollHeaddressProfitableProductsRecyclerViewLogic() {
        timer = Timer()
        timerTask = object: TimerTask() {
            override fun run() {
                autoScrollRecyclerViewLogic(binding.headdressProfitableProductsRecyclerView, headdressProfitableAdapter, headdressProfitableLinearLayoutManager)
            }
        }
        timer.schedule(timerTask, 0, RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
    }

    private fun setupHeaddressOtherRecyclerView() {
        headdressOtherAdapter = InterestingProductsAdapter()
        binding.headdressOtherProductsRecyclerView.apply {
            adapter = headdressOtherAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            headdressOtherAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun headdressOtherProductsRecyclerViewReachedBottom() { productRecyclerViewReachedBottomLogic(binding.headdressNestedScrollView) { headdressCategoryViewModel.getHeaddressOtherProducts() } }

    private fun onHeaddressProfitableProductClick() {
        headdressProfitableAdapter.onProductClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product)
            findNavController().navigate(action)
        }
    }

    private fun onHeaddressOtherProductClick() {
        headdressOtherAdapter.onProductClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product)
            findNavController().navigate(action)
        }
    }

    private fun observeHeaddressCategoryState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    headdressCategoryViewModel.headdressProfitableProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideHeaddressProfitableProductsProgressBar()
                                headdressProfitableAdapter.differ.submitList(it.data)
                            }
                            is Resource.Loading -> showHeaddressProfitableProductsProgressBar()
                            is Resource.Error -> {
                                hideHeaddressProfitableProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
                launch {
                    headdressCategoryViewModel.headdressOtherProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideHeaddressOtherProductsProgressBar()
                                headdressOtherAdapter.differ.submitList(it.data)
                            }
                            is Resource.Loading -> showHeaddressOtherProductsProgressBar()
                            is Resource.Error -> {
                                hideHeaddressOtherProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun showHeaddressProfitableProductsProgressBar() {
        binding.headdressProfitableProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideHeaddressProfitableProductsProgressBar() {
        binding.headdressProfitableProductsProgressBar.visibility = View.GONE
    }

    private fun showHeaddressOtherProductsProgressBar() {
        binding.headdressOtherProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideHeaddressOtherProductsProgressBar() {
        binding.headdressOtherProductsProgressBar.visibility = View.GONE
    }
}