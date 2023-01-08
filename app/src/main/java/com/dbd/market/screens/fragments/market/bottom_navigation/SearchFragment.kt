package com.dbd.market.screens.fragments.market.bottom_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.search.SearchAdapter
import com.dbd.market.databinding.FragmentSearchBinding
import com.dbd.market.utils.MarginItemDecoration
import com.dbd.market.utils.MarginItemDecorationType
import com.dbd.market.utils.Resource
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var searchAdapter: SearchAdapter
    private var searchProductsJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeHeaderSearchViewBehaviour()
        setupSearchAdapter()
        searchProducts()
        observeSearchProductsState()
    }

    private fun changeHeaderSearchViewBehaviour() { binding.headerSearchView.setIconifiedByDefault(false) }

    private fun setupSearchAdapter() {
        searchAdapter = SearchAdapter(requireContext())
        binding.searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.PRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInCartProductRecyclerView)))
        }
    }

    private fun searchProducts() {
        binding.headerSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchQuery ->
                    if (searchQuery.isNotEmpty()) {
                        searchProductsJob?.cancel()
                        searchProductsJob = viewLifecycleOwner.lifecycleScope.launch {
                            delay(500L)
                            searchViewModel.searchProducts(searchQuery)
                        }
                    } else searchAdapter.differ.submitList(emptyList())
                }
                return true
            }
        })
    }

    private fun observeSearchProductsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchProducts.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest {
                when(it) {
                    is Resource.Success -> {
                        val listOfSearchedProducts = it.data
                        if (listOfSearchedProducts != null && listOfSearchedProducts.isNotEmpty()) {
                            hideSearchProgressBar()
                            hideEmptyWidgets()
                            searchAdapter.differ.submitList(listOfSearchedProducts)
                        } else {
                            hideSearchProgressBar()
                            showEmptyWidgets()
                        }
                    }
                    is Resource.Loading -> {
                        searchAdapter.differ.submitList(emptyList())
                        showSearchProgressBar()
                        hideEmptyWidgets()
                    }
                    is Resource.Error -> {
                        hideSearchProgressBar()
                        hideEmptyWidgets()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideEmptyWidgets()
    }

    private fun showSearchProgressBar() { binding.searchProgressBar.visibility = View.VISIBLE }

    private fun hideSearchProgressBar() { binding.searchProgressBar.visibility = View.GONE }

    private fun showEmptyWidgets() {
        binding.apply {
            searchEmptyImageView.visibility = View.VISIBLE
            searchEmptyTextView.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyWidgets() {
        binding.apply {
            searchEmptyImageView.visibility = View.GONE
            searchEmptyTextView.visibility = View.GONE
        }
    }
}