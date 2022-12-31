package com.dbd.market.screens.fragments.market

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.addresses.AddressesAdapter
import com.dbd.market.databinding.FragmentAddressesBinding
import com.dbd.market.utils.MarginItemDecoration
import com.dbd.market.utils.MarginItemDecorationType
import com.dbd.market.utils.Resource
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.AddressesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressesFragment : Fragment() {
    private lateinit var binding: FragmentAddressesBinding
    private val addressesViewModel by viewModels<AddressesViewModel>()
    private lateinit var addressesAdapter: AddressesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddressesRecyclerView()
        onCloseAddressesImageViewClick()
        observeAddressesState()
    }

    private fun setupAddressesRecyclerView() {
        addressesAdapter = AddressesAdapter()
        binding.addressesRecyclerView.apply {
            adapter = addressesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.CARTPRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun onCloseAddressesImageViewClick() { binding.closeAddresses.setOnClickListener { findNavController().navigateUp() } }

    private fun observeAddressesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            addressesViewModel.allAddresses.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when(it) {
                    is Resource.Success -> {
                        hideAddressesProgressBar()
                        val listOfAddresses = it.data
                        listOfAddresses?.let { addresses ->
                            if (addresses.isNotEmpty()) addressesAdapter.differ.submitList(addresses)
                            else showAddressesEmptyListWidgets()
                        }
                    }
                    is Resource.Loading -> showAddressesProgressBar()
                    is Resource.Error -> {
                        hideAddressesProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun showAddressesProgressBar() { binding.addressesProgressBar.visibility = View.VISIBLE }

    private fun hideAddressesProgressBar() { binding.addressesProgressBar.visibility = View.GONE }

    private fun showAddressesEmptyListWidgets() {
        binding.apply {
            addressesEmptyImageView.visibility = View.VISIBLE
            addressesEmptyTextView.visibility = View.VISIBLE
        }
    }

}