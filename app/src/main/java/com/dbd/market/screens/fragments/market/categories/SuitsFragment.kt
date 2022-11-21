package com.dbd.market.screens.fragments.market.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R
import com.dbd.market.adapters.main_category.InterestingProductsAdapter
import com.dbd.market.adapters.main_category.ProfitableCategoryProductsAdapter
import com.dbd.market.databinding.FragmentSuitsBinding
import com.dbd.market.utils.*
import com.dbd.market.viewmodels.market.categories.suits.SuitsCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SuitsFragment: BaseCategoryFragment<FragmentSuitsBinding>(FragmentSuitsBinding::inflate) {

    private val suitsCategoryViewModel by viewModels<SuitsCategoryViewModel>()
    private lateinit var suitsProfitableProductsAdapter: ProfitableCategoryProductsAdapter
    private lateinit var suitsOtherProductsAdapter: InterestingProductsAdapter
    private lateinit var suitsProfitableProductsLinearLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSuitsProfitableProductsRecyclerView()
        setupSuitsOtherProductsRecyclerView()
        observeSuitsCategoryState()
        suitsOtherProductsRecyclerViewReachedBottomLogic()
    }

    private fun setupSuitsProfitableProductsRecyclerView() {
        suitsProfitableProductsAdapter = ProfitableCategoryProductsAdapter()
        suitsProfitableProductsLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.suitsProfitableProductsRecyclerView.apply {
            adapter = suitsProfitableProductsAdapter
            layoutManager = suitsProfitableProductsLinearLayoutManager
        }
        autoScrollSuitsProfitableProductsRecyclerViewLogic()
    }

    private fun setupSuitsOtherProductsRecyclerView() {
        suitsOtherProductsAdapter = InterestingProductsAdapter()
        binding.suitsOtherProductsRecyclerView.apply {
            adapter = suitsOtherProductsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            suitsOtherProductsAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
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

    private fun suitsOtherProductsRecyclerViewReachedBottomLogic() { productRecyclerViewReachedBottomLogic(binding.suitsNestedScrollView) { suitsCategoryViewModel.getSuitsOtherProducts() } }

    private fun observeSuitsCategoryState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    suitsCategoryViewModel.suitsProfitableProducts.collect {
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
                launch {
                    suitsCategoryViewModel.suitsOtherProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideSuitsOtherProductsProgressBar()
                                suitsOtherProductsAdapter.differ.submitList(it.data)
                            }
                            is Resource.Loading -> showSuitsOtherProductsProgressBar()
                            is Resource.Error -> {
                                hideSuitsOtherProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
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

    private fun showSuitsOtherProductsProgressBar() {
        binding.suitsOtherProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideSuitsOtherProductsProgressBar() {
        binding.suitsOtherProductsProgressBar.visibility = View.GONE
    }

}