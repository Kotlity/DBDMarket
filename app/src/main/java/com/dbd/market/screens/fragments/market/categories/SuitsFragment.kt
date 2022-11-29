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
import com.dbd.market.databinding.FragmentSuitsBinding
import com.dbd.market.screens.fragments.market.HomeFragmentDirections
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
        onSuitsProfitableProductClick()
        onSuitsOtherProductClick()
    }

    override fun onResume() {
        super.onResume()
        autoScrollSuitsProfitableProductsRecyclerViewLogic()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        timerTask.cancel()
    }

    private fun setupSuitsProfitableProductsRecyclerView() {
        suitsProfitableProductsAdapter = ProfitableCategoryProductsAdapter()
        suitsProfitableProductsLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.suitsProfitableProductsRecyclerView.apply {
            adapter = suitsProfitableProductsAdapter
            layoutManager = suitsProfitableProductsLinearLayoutManager
        }
    }

    private fun autoScrollSuitsProfitableProductsRecyclerViewLogic() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() { autoScrollRecyclerViewLogic(binding.suitsProfitableProductsRecyclerView, suitsProfitableProductsAdapter, suitsProfitableProductsLinearLayoutManager) }
        }
        timer.schedule(timerTask, 0, Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
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

    private fun suitsOtherProductsRecyclerViewReachedBottomLogic() { productRecyclerViewReachedBottomLogic(binding.suitsNestedScrollView) { suitsCategoryViewModel.getSuitsOtherProducts() } }

    private fun onSuitsProfitableProductClick() {
        suitsProfitableProductsAdapter.onProductClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product)
            findNavController().navigate(action)
        }
    }

    private fun onSuitsOtherProductClick() {
        suitsOtherProductsAdapter.onProductClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product)
            findNavController().navigate(action)
        }
    }

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