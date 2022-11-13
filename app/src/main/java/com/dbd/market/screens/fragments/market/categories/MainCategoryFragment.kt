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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.main_category.BeneficialProductsAdapter
import com.dbd.market.adapters.main_category.InterestingProductsAdapter
import com.dbd.market.adapters.main_category.MarginItemDecoration
import com.dbd.market.adapters.main_category.SpecialProductsAdapter
import com.dbd.market.databinding.FragmentMainCategoryBinding
import com.dbd.market.utils.Resource
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.categories.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var beneficialProductsAdapter: BeneficialProductsAdapter
    private lateinit var interestingProductsAdapter: InterestingProductsAdapter
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
    }

    private fun setupSpecialProductsRecyclerView() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.specialProductsRecyclerView.apply {
            adapter = specialProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupBeneficialProductsRecyclerView() {
        beneficialProductsAdapter = BeneficialProductsAdapter()
        binding.beneficialProductsRecyclerView.apply {
            adapter = beneficialProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupInterestingProductsRecyclerView() {
        interestingProductsAdapter = InterestingProductsAdapter()
        binding.interestingProductsRecyclerView.apply {
            adapter = interestingProductsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInInterestingProductsRecyclerView)))
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