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
import com.dbd.market.adapters.suits.ProfitableCategoryProductsAdapter
import com.dbd.market.databinding.FragmentWeaponBinding
import com.dbd.market.utils.Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD
import com.dbd.market.utils.Resource
import com.dbd.market.utils.autoScrollRecyclerViewLogic
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.categories.weapons.WeaponsCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class WeaponFragment: BaseCategoryFragment<FragmentWeaponBinding>(FragmentWeaponBinding::inflate) {

    private val weaponsCategoryViewModel by viewModels<WeaponsCategoryViewModel>()
    private lateinit var weaponsProfitableProductsAdapter: ProfitableCategoryProductsAdapter
    private lateinit var weaponsProfitableProductsLinearLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWeaponsProfitableProductsAdapter()
        observeWeaponsCategoryState()
    }

    private fun setupWeaponsProfitableProductsAdapter() {
        weaponsProfitableProductsAdapter = ProfitableCategoryProductsAdapter()
        weaponsProfitableProductsLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.weaponProfitableProductsRecyclerView.apply {
            adapter = weaponsProfitableProductsAdapter
            layoutManager = weaponsProfitableProductsLinearLayoutManager
        }
        autoScrollWeaponsProfitableProductsRecyclerViewLogic()
    }

    private fun autoScrollWeaponsProfitableProductsRecyclerViewLogic() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.weaponProfitableProductsRecyclerView)
        timer = Timer()
        timerTask = object: TimerTask() {
            override fun run() {
                autoScrollRecyclerViewLogic(binding.weaponProfitableProductsRecyclerView, weaponsProfitableProductsAdapter, weaponsProfitableProductsLinearLayoutManager)
            }
        }
        timer.schedule(timerTask, 0, RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
    }

    private fun observeWeaponsCategoryState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    weaponsCategoryViewModel.weaponsProfitableProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideWeaponsProfitableProductsProgressbar()
                                weaponsProfitableProductsAdapter.differ.submitList(it.data)
                            }
                            is Resource.Loading -> showWeaponsProfitableProductsProgressbar()
                            is Resource.Error -> {
                                hideWeaponsProfitableProductsProgressbar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun showWeaponsProfitableProductsProgressbar() {
        binding.weaponProfitableProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideWeaponsProfitableProductsProgressbar() {
        binding.weaponProfitableProductsProgressBar.visibility = View.GONE
    }
}