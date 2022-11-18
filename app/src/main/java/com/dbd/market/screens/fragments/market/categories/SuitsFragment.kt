package com.dbd.market.screens.fragments.market.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.dbd.market.R
import com.dbd.market.adapters.suits.SuitsProfitableProductsAdapter
import com.dbd.market.databinding.FragmentSuitsBinding
import com.dbd.market.utils.Constants
import com.dbd.market.utils.Resource
import com.dbd.market.utils.autoScrollRecyclerViewLogic
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.categories.suits.SuitsCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SuitsFragment: BaseCategoryFragment<FragmentSuitsBinding>(FragmentSuitsBinding::inflate) {

    private val suitsCategoryViewModel by viewModels<SuitsCategoryViewModel>()
    private lateinit var suitsProfitableProductsAdapter: SuitsProfitableProductsAdapter
    private lateinit var suitsProfitableProductsLinearLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSuitsProfitableProductsRecyclerView()
        observeSuitsCategoryState()
    }

    private fun setupSuitsProfitableProductsRecyclerView() {
        suitsProfitableProductsAdapter = SuitsProfitableProductsAdapter()
        suitsProfitableProductsLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.suitsProfitableProductsRecyclerView.apply {
            adapter = suitsProfitableProductsAdapter
            layoutManager = suitsProfitableProductsLinearLayoutManager
        }
        autoScrollSuitsProfitableProductsRecyclerViewLogic()
    }

    private fun autoScrollSuitsProfitableProductsRecyclerViewLogic() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.suitsOtherProductsRecyclerView)
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() { autoScrollRecyclerViewLogic(binding.suitsProfitableProductsRecyclerView, suitsProfitableProductsAdapter, suitsProfitableProductsLinearLayoutManager) }
        }
        timer.schedule(timerTask, 0, Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
    }

    private fun observeSuitsCategoryState() {
        viewLifecycleOwner.lifecycleScope.launch {
            suitsCategoryViewModel.suitsProfitableProducts.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        hideSuitsProfitableProductsProgressBar()
                        suitsProfitableProductsAdapter.differ.submitList(it.data)
                    }
                    is Resource.Loading -> showSuitsProfitableProductsProgressBar()
                    is Resource.Error -> {
                        hideSuitsProfitableProductsProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun showSuitsProfitableProductsProgressBar() {
        binding.suitsProfitableProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideSuitsProfitableProductsProgressBar() {
        binding.suitsProfitableProductsProgressBar.visibility = View.GONE
    }

}