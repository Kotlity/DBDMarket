package com.dbd.market.screens.fragments.market.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.dbd.market.R
import com.dbd.market.adapters.main_category.ProfitableCategoryProductsAdapter
import com.dbd.market.databinding.FragmentHeaddressBinding
import com.dbd.market.utils.Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD
import com.dbd.market.utils.Resource
import com.dbd.market.utils.autoScrollRecyclerViewLogic
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.categories.HeaddressCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HeaddressFragment: BaseCategoryFragment<FragmentHeaddressBinding>(FragmentHeaddressBinding::inflate) {

    private val headdressCategoryViewModel by viewModels<HeaddressCategoryViewModel>()
    private lateinit var headdressProfitableAdapter: ProfitableCategoryProductsAdapter
    private lateinit var headdressProfitableLinearLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeaddressProfitableRecyclerView()
        observeHeaddressCategoryState()
    }

    private fun setupHeaddressProfitableRecyclerView() {
        headdressProfitableAdapter = ProfitableCategoryProductsAdapter()
        headdressProfitableLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.headdressProfitableProductsRecyclerView.apply {
            adapter = headdressProfitableAdapter
            layoutManager = headdressProfitableLinearLayoutManager
        }
        autoScrollHeaddressProfitableProductsRecyclerViewLogic()
    }

    private fun autoScrollHeaddressProfitableProductsRecyclerViewLogic() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.headdressProfitableProductsRecyclerView)
        timer = Timer()
        timerTask = object: TimerTask() {
            override fun run() {
                autoScrollRecyclerViewLogic(binding.headdressProfitableProductsRecyclerView, headdressProfitableAdapter, headdressProfitableLinearLayoutManager)
            }
        }
        timer.schedule(timerTask, 0, RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
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
            }
        }
    }

    private fun showHeaddressProfitableProductsProgressBar() {
        binding.headdressProfitableProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideHeaddressProfitableProductsProgressBar() {
        binding.headdressProfitableProductsProgressBar.visibility = View.GONE
    }
}