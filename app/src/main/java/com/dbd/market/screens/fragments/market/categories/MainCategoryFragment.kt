package com.dbd.market.screens.fragments.market.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.dbd.market.R
import com.dbd.market.adapters.main_category.BeneficialProductsAdapter
import com.dbd.market.adapters.main_category.InterestingProductsAdapter
import com.dbd.market.adapters.main_category.SpecialProductsAdapter
import com.dbd.market.data.Product
import com.dbd.market.databinding.FragmentMainCategoryBinding
import com.dbd.market.screens.fragments.market.bottom_navigation.HomeFragmentDirections
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.RECYCLER_VIEW_AUTO_SCROLL_PERIOD
import com.dbd.market.viewmodels.market.categories.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var beneficialProductsAdapter: BeneficialProductsAdapter
    private lateinit var interestingProductsAdapter: InterestingProductsAdapter
    lateinit var specialProductsLayoutManager: LinearLayoutManager
    lateinit var beneficialProductsLayoutManager: LinearLayoutManager
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask
    private val mainCategoryViewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpecialProductsRecyclerView()
        setupBeneficialProductsRecyclerView()
        setupInterestingProductsRecyclerView()
        observeMainCategoryProductsState()
        observeMainCategoryProductsErrorState()
        interestingProductsRecyclerViewReachedBottom()
        beneficialProductsRecyclerViewReachedLeft()
        onSpecialProductClick()
        onBeneficialProductClick()
        onInterestingProductClick()
    }

    override fun onResume() {
        super.onResume()
        autoScrollSpecialProductsRecyclerViewLogic()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        timerTask.cancel()
    }

    private fun setupSpecialProductsRecyclerView() {
        specialProductsAdapter = SpecialProductsAdapter()
        specialProductsLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.specialProductsRecyclerView.apply {
            adapter = specialProductsAdapter
            layoutManager = specialProductsLayoutManager
        }
    }

    private fun autoScrollSpecialProductsRecyclerViewLogic() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() { autoScrollRecyclerViewLogic(binding.specialProductsRecyclerView, specialProductsAdapter, specialProductsLayoutManager) }
        }
        timer.schedule(timerTask, 0, RECYCLER_VIEW_AUTO_SCROLL_PERIOD)
    }

    private fun setupBeneficialProductsRecyclerView() {
        beneficialProductsAdapter = BeneficialProductsAdapter()
        beneficialProductsLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.beneficialProductsRecyclerView.apply {
            adapter = beneficialProductsAdapter
            layoutManager = beneficialProductsLayoutManager
            beneficialProductsAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    private fun beneficialProductsRecyclerViewReachedLeft() {
        productRecyclerViewReachedRightLogic(binding.beneficialProductsRecyclerView) {
            mainCategoryViewModel.fetchBeneficialProductsFromFirebaseFirestore()
        }
    }

    private fun setupInterestingProductsRecyclerView() {
        interestingProductsAdapter = InterestingProductsAdapter()
        binding.interestingProductsRecyclerView.apply {
            adapter = interestingProductsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            interestingProductsAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.PRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun interestingProductsRecyclerViewReachedBottom() {
        productRecyclerViewReachedBottomLogic(binding.mainCategoryNestedScrollView) {
            mainCategoryViewModel.fetchInterestingProductsFromFirebaseFirestore()
        }
    }

    private fun onSpecialProductClick() {
        specialProductsAdapter.onRecyclerViewItemClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product as Product )
            findNavController().navigate(action)
        }
    }

    private fun onBeneficialProductClick() {
        beneficialProductsAdapter.onRecyclerViewItemClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product as Product)
            findNavController().navigate(action)
        }
    }

    private fun onInterestingProductClick() {
        interestingProductsAdapter.onRecyclerViewItemClick { product ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDescriptionFragment(product as Product)
            findNavController().navigate(action)
        }
    }

    private fun observeMainCategoryProductsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainCategoryViewModel.specialProducts.collect {
                        when (it) {
                            is Resource.Loading -> { showSpecialProductsProgressBar() }
                            is Resource.Success -> {
                                hideSpecialProductsProgressBar()
                                specialProductsAdapter.differ.submitList(it.data)
                            }
                            is Resource.Error -> {
                                hideSpecialProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
                launch {
                    mainCategoryViewModel.beneficialProducts.collect {
                        when (it) {
                            is Resource.Loading -> { showBeneficialProductsProgressBar() }
                            is Resource.Success -> {
                                hideBeneficialProductsProgressBar()
                                beneficialProductsAdapter.differ.submitList(it.data)
                            }
                            is Resource.Error -> {
                                hideBeneficialProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
                launch {
                    mainCategoryViewModel.interestingProducts.collect {
                        when (it) {
                            is Resource.Loading -> { showInterestingProductsProgressBar()}
                            is Resource.Success -> {
                                hideInterestingProductsProgressBar()
                                interestingProductsAdapter.differ.submitList(it.data)
                            }
                            is Resource.Error -> {
                                hideInterestingProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun observeMainCategoryProductsErrorState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainCategoryViewModel.specialProductsError.collect {
                        when (it) {
                            true -> {
                                hideSpecialProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, getString(R.string.specialProductsListIsEmptyError))
                            }
                            else -> Unit
                        }
                    }
                }
                launch {
                    mainCategoryViewModel.beneficialProductsError.collect {
                        when (it) {
                            true -> {
                                hideBeneficialProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, getString(R.string.beneficialProductsListIsEmptyError))
                            }
                            else -> Unit
                        }
                    }
                }
                launch {
                    mainCategoryViewModel.interestingProductsError.collect {
                        when (it) {
                            true -> {
                                hideInterestingProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, getString(R.string.interestingProductsListIsEmptyError))
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun showSpecialProductsProgressBar() {
        binding.specialProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideSpecialProductsProgressBar() {
        binding.specialProductsProgressBar.visibility = View.GONE
    }

    private fun showBeneficialProductsProgressBar() {
        binding.beneficialProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideBeneficialProductsProgressBar() {
        binding.beneficialProductsProgressBar.visibility = View.GONE
    }

    private fun showInterestingProductsProgressBar() {
        binding.interestingProductsProgressBar.visibility = View.VISIBLE
    }

    private fun hideInterestingProductsProgressBar() {
        binding.interestingProductsProgressBar.visibility = View.GONE
    }

}