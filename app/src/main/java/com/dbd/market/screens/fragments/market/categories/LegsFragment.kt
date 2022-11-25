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
import com.dbd.market.databinding.FragmentLegsBinding
import com.dbd.market.utils.*
import com.dbd.market.viewmodels.market.categories.legs.LegsCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class LegsFragment: BaseCategoryFragment<FragmentLegsBinding>(FragmentLegsBinding::inflate){

    private val legsCategoryViewModel by viewModels<LegsCategoryViewModel>()
    private lateinit var legsProfitableAdapter: ProfitableCategoryProductsAdapter
    private lateinit var legsOtherAdapter: InterestingProductsAdapter
    private lateinit var legsProfitableLinearLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLegsProfitableAdapter()
        setupLegsOtherAdapter()
        observeLegsCategoryState()
        legsOtherProductsRecyclerViewReachedBottom()
    }

    private fun setupLegsProfitableAdapter() {
        legsProfitableAdapter = ProfitableCategoryProductsAdapter()
        legsProfitableLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.legsProfitableProductsRecyclerView.apply {
            adapter = legsProfitableAdapter
            layoutManager = legsProfitableLinearLayoutManager
        }
        autoScrollLegsProfitableProductsRecyclerViewLogic()
    }

    private fun setupLegsOtherAdapter() {
        legsOtherAdapter = InterestingProductsAdapter()
        binding.legsOtherProductsRecyclerView.apply {
            adapter = legsOtherAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            legsOtherAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun autoScrollLegsProfitableProductsRecyclerViewLogic() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.legsProfitableProductsRecyclerView)
        timer = Timer()
        timerTask = object: TimerTask() {
            override fun run() {
                autoScrollRecyclerViewLogic(binding.legsProfitableProductsRecyclerView, legsProfitableAdapter, legsProfitableLinearLayoutManager)
            }
        }
        timer.schedule(timerTask, 0, Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
    }

    private fun legsOtherProductsRecyclerViewReachedBottom() { productRecyclerViewReachedBottomLogic(binding.legsNestedScrollView) { legsCategoryViewModel.getLegsOtherProducts() } }

    private fun observeLegsCategoryState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    legsCategoryViewModel.legsProfitableProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideLegsProfitableProductsProgressBar()
                                legsProfitableAdapter.differ.submitList(it.data)
                            }
                            is Resource.Loading -> showLegsProfitableProductsProgressBar()
                            is Resource.Error -> {
                                hideLegsProfitableProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
                launch {
                    legsCategoryViewModel.legsOtherProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideLegsOtherProductsProgressBar()
                                legsOtherAdapter.differ.submitList(it.data)
                            }
                            is Resource.Loading -> showLegsOtherProductsProgressBar()
                            is Resource.Error -> {
                                hideLegsOtherProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun showLegsProfitableProductsProgressBar() {
        binding.legsProfitableProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideLegsProfitableProductsProgressBar() {
        binding.legsProfitableProductsProgressBar.visibility = View.GONE
    }

    private fun showLegsOtherProductsProgressBar() {
        binding.legsOtherProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideLegsOtherProductsProgressBar() {
        binding.legsOtherProductsProgressBar.visibility = View.GONE
    }
}