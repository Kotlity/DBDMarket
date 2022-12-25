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
import com.dbd.market.data.Product
import com.dbd.market.databinding.FragmentTorsoBinding
import com.dbd.market.screens.fragments.market.bottom_navigation.HomeFragmentDirections
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD
import com.dbd.market.viewmodels.market.categories.TorsoCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class TorsoFragment: BaseCategoryFragment<FragmentTorsoBinding>(FragmentTorsoBinding::inflate) {

    private val torsoCategoryViewModel by viewModels<TorsoCategoryViewModel>()
    private lateinit var torsoProfitableAdapter: ProfitableCategoryProductsAdapter
    private lateinit var torsoOtherAdapter: InterestingProductsAdapter
    private lateinit var torsoProfitableLinearLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTorsoProfitableAdapter()
        setupTorsoOtherAdapter()
        observeTorsoCategoryState()
        torsoOtherProductsRecyclerViewReachedBottom()
        onTorsoProfitableProductClick()
        onTorsoOtherProductClick()
    }

    override fun onResume() {
        super.onResume()
        autoScrollTorsoProfitableProductsRecyclerViewLogic()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        timerTask.cancel()
    }

    private fun setupTorsoProfitableAdapter() {
        torsoProfitableAdapter = ProfitableCategoryProductsAdapter()
        torsoProfitableLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.torsoProfitableProductsRecyclerView.apply {
            adapter = torsoProfitableAdapter
            layoutManager = torsoProfitableLinearLayoutManager
        }
    }

    private fun autoScrollTorsoProfitableProductsRecyclerViewLogic() {
        timer = Timer()
        timerTask = object: TimerTask() {
            override fun run() {
                autoScrollRecyclerViewLogic(binding.torsoProfitableProductsRecyclerView, torsoProfitableAdapter, torsoProfitableLinearLayoutManager)
            }
        }
        timer.schedule(timerTask, 0, RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
    }

    private fun setupTorsoOtherAdapter() {
        torsoOtherAdapter = InterestingProductsAdapter()
        binding.torsoOtherProductsRecyclerView.apply {
            adapter = torsoOtherAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            torsoOtherAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.PRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun torsoOtherProductsRecyclerViewReachedBottom() { productRecyclerViewReachedBottomLogic(binding.torsoNestedScrollView) { torsoCategoryViewModel.getTorsoOtherProducts() } }

    private fun onTorsoProfitableProductClick() {
        torsoProfitableAdapter.onRecyclerViewItemClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product as Product)
            findNavController().navigate(action)
        }
    }

    private fun onTorsoOtherProductClick() {
        torsoOtherAdapter.onRecyclerViewItemClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product as Product)
            findNavController().navigate(action)
        }
    }

    private fun observeTorsoCategoryState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    torsoCategoryViewModel.torsoProfitableProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideTorsoProfitableProductsProgressBar()
                                torsoProfitableAdapter.differ.submitList(it.data)
                            }
                            is Resource.Loading -> showTorsoProfitableProductsProgressBar()
                            is Resource.Error -> {
                                hideTorsoProfitableProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
                launch {
                    torsoCategoryViewModel.torsoOtherProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideTorsoOtherProductsProgressBar()
                                torsoOtherAdapter.differ.submitList(it.data)
                            }
                            is Resource.Loading -> showTorsoOtherProductsProgressBar()
                            is Resource.Error -> {
                                hideTorsoOtherProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun showTorsoProfitableProductsProgressBar() {
        binding.torsoProfitableProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideTorsoProfitableProductsProgressBar() {
        binding.torsoProfitableProductsProgressBar.visibility = View.GONE
    }

    private fun showTorsoOtherProductsProgressBar() {
        binding.torsoOtherProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideTorsoOtherProductsProgressBar() {
        binding.torsoOtherProductsProgressBar.visibility = View.GONE
    }
}