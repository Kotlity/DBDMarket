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
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.setup_order.SetupOrderAddressesAdapter
import com.dbd.market.adapters.setup_order.SetupOrderCartProductsAdapter
import com.dbd.market.data.Address
import com.dbd.market.databinding.FragmentSetupOrderBinding
import com.dbd.market.utils.*
import com.dbd.market.viewmodels.market.SetupOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetupOrderFragment : Fragment() {
    private lateinit var binding: FragmentSetupOrderBinding
    private val args by navArgs<SetupOrderFragmentArgs>()
    private val setupOrderViewModel by viewModels<SetupOrderViewModel>()
    private lateinit var setupOrderCartProductsAdapter: SetupOrderCartProductsAdapter
    private lateinit var setupOrderAddressesAdapter: SetupOrderAddressesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetupOrderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrderCartProductsRecyclerView()
        setupOrderAddressesRecyclerView()
        setTotalPriceTextView()
        closeSetupOrderFragment()
        showCustomBottomSheetDialog()
        observeSetupOrderStates()
    }

    private fun setupOrderCartProductsRecyclerView() {
        setupOrderCartProductsAdapter = SetupOrderCartProductsAdapter(requireContext())
        binding.setupOrderCartProductsRecyclerView.apply {
            adapter = setupOrderCartProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.CARTPRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun setupOrderAddressesRecyclerView() {
        setupOrderAddressesAdapter = SetupOrderAddressesAdapter()
        binding.setupOrderAddressesRecyclerView.apply {
            adapter = setupOrderAddressesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.SIZE, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun addAddress(address: Address) {
        setupOrderViewModel.addAddress(address)
        observeAddAddressState()
    }

    private fun setTotalPriceTextView() { binding.setupOrderTotalPriceTextView.text = args.cartProductsSetupOrder.totalPrice.toString().plus("$") }

    private fun closeSetupOrderFragment() { binding.closeSetupOrder.setOnClickListener { requireActivity().onBackPressed() } }

    private fun showCustomBottomSheetDialog() {
        binding.chooseAddressImageView.setOnClickListener { showBottomSheetDialog(requireContext(), onSuccess = { addedAddress -> addAddress(addedAddress) }) }
    }

    private fun observeSetupOrderStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    setupOrderViewModel.setupOrderCartProducts.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideSetupOrderCartProductsProgressBar()
                                setupOrderCartProductsAdapter.differ.submitList(args.cartProductsSetupOrder.cartProductList)
                            }
                            is Resource.Loading -> showSetupOrderCartProductsProgressBar()
                            is Resource.Error -> {
                                hideSetupOrderCartProductsProgressBar()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
                launch {
                    setupOrderViewModel.setupOrderAddresses.collect {
                        when (it) {
                            is Resource.Success -> {
                                hideSetupOrderAddressesProgressBar()
                                it.data?.let { listOfAddresses ->
                                    if (listOfAddresses.isNotEmpty()) {
                                        setupOrderAddressesAdapter.differ.submitList(listOfAddresses)
                                        showChooseAddressWarningTextView()
                                        showSetupOrderAddressesRecyclerView()

                                    } else {
                                        setupOrderAddressesAdapter.differ.submitList(listOfAddresses)
                                        hideChooseAddressWarningTextView()
                                        hideSetupOrderAddressesRecyclerView()
                                    }
                                }
                            }
                            is Resource.Loading -> {
                                hideChooseAddressWarningTextView()
                                showSetupOrderAddressesProgressBar()
                            }
                            is Resource.Error -> {
                                hideChooseAddressWarningTextView()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
            }
//            setupOrderViewModel.setupOrderCartProducts.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
//                when (it) {
//                    is Resource.Success -> {
//                        hideSetupOrderProgressBar()
//                        setupOrderCartProductsAdapter.differ.submitList(args.cartProductsSetupOrder.cartProductList)
//                    }
//                    is Resource.Loading -> showSetupOrderProgressBar()
//                    is Resource.Error -> {
//                        hideSetupOrderProgressBar()
//                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
//                    }
//                    is Resource.Undefined -> Unit
//                }
//            }
        }
    }

    private fun observeAddAddressState() {
        viewLifecycleOwner.lifecycleScope.launch {
            setupOrderViewModel.setupOrderAddedAddress.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        hideSetupOrderAddingDeletingProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_done_icon, "You have successfully added the address")
                    }
                    is Resource.Loading -> showSetupOrderAddingDeletingProgressBar()
                    is Resource.Error -> {
                        hideSetupOrderAddingDeletingProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun showSetupOrderCartProductsProgressBar() { binding.setupOrderCartProductsProgressBar.visibility = View.VISIBLE }

    private fun hideSetupOrderCartProductsProgressBar() { binding.setupOrderCartProductsProgressBar.visibility = View.GONE }

    private fun showSetupOrderAddressesProgressBar() { binding.setupOrderAddressesProgressBar.visibility = View.VISIBLE }

    private fun hideSetupOrderAddressesProgressBar() { binding.setupOrderAddressesProgressBar.visibility = View.GONE }

    private fun showChooseAddressWarningTextView() { binding.chooseAddressWarningTextView.visibility = View.VISIBLE }

    private fun hideChooseAddressWarningTextView() { binding.chooseAddressWarningTextView.visibility = View.GONE }

    private fun showSetupOrderAddressesRecyclerView() { binding.setupOrderAddressesRecyclerView.visibility = View.VISIBLE }

    private fun hideSetupOrderAddressesRecyclerView() { binding.setupOrderAddressesRecyclerView.visibility = View.GONE }

    private fun showSetupOrderAddingDeletingProgressBar() { binding.setupOrderAddingDeletingProgressBar.visibility = View.VISIBLE }

    private fun hideSetupOrderAddingDeletingProgressBar() { binding.setupOrderAddingDeletingProgressBar.visibility = View.GONE }
}