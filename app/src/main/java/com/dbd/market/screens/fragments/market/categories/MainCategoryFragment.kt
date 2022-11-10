package com.dbd.market.screens.fragments.market.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
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
        observeSpecialProductsState()
    }

    private fun setupSpecialProductsRecyclerView() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.specialProductsRecyclerView.apply {
            adapter = specialProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeSpecialProductsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainCategoryViewModel.specialProducts.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        specialProductsAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }
}