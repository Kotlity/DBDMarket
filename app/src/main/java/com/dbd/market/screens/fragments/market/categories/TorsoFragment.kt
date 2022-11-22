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
import com.dbd.market.databinding.FragmentTorsoBinding
import com.dbd.market.utils.Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD
import com.dbd.market.utils.Resource
import com.dbd.market.utils.autoScrollRecyclerViewLogic
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.categories.torso.TorsoCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class TorsoFragment: BaseCategoryFragment<FragmentTorsoBinding>(FragmentTorsoBinding::inflate) {

    private val torsoCategoryViewModel by viewModels<TorsoCategoryViewModel>()
    private lateinit var torsoProfitableAdapter: ProfitableCategoryProductsAdapter
    private lateinit var torsoProfitableLinearLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTorsoProfitableAdapter()
        observeTorsoCategoryState()
    }

    private fun setupTorsoProfitableAdapter() {
        torsoProfitableAdapter = ProfitableCategoryProductsAdapter()
        torsoProfitableLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.torsoProfitableProductsRecyclerView.apply {
            adapter = torsoProfitableAdapter
            layoutManager = torsoProfitableLinearLayoutManager
        }
        autoScrollTorsoProfitableProductsRecyclerViewLogic()
    }

    private fun autoScrollTorsoProfitableProductsRecyclerViewLogic() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.torsoProfitableProductsRecyclerView)
        timer = Timer()
        timerTask = object: TimerTask() {
            override fun run() {
                autoScrollRecyclerViewLogic(binding.torsoProfitableProductsRecyclerView, torsoProfitableAdapter, torsoProfitableLinearLayoutManager)
            }
        }
        timer.schedule(timerTask, 0, RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
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
            }
        }
    }

    private fun showTorsoProfitableProductsProgressBar() {
        binding.torsoProfitableProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideTorsoProfitableProductsProgressBar() {
        binding.torsoProfitableProductsProgressBar.visibility = View.GONE
    }
}