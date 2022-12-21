package com.dbd.market.screens.fragments.market

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.setup_order.SetupOrderAddressesAdapter
import com.dbd.market.adapters.setup_order.SetupOrderCartProductsAdapter
import com.dbd.market.data.Address
import com.dbd.market.data.CartProductsSetupOrder
import com.dbd.market.data.Order
import com.dbd.market.databinding.FragmentSetupOrderBinding
import com.dbd.market.utils.*
import com.dbd.market.viewmodels.market.SetupOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SetupOrderFragment : Fragment() {
    private lateinit var binding: FragmentSetupOrderBinding
    private val args by navArgs<SetupOrderFragmentArgs>()
    private val setupOrderViewModel by viewModels<SetupOrderViewModel>()
    private lateinit var setupOrderCartProductsAdapter: SetupOrderCartProductsAdapter
    private lateinit var setupOrderAddressesAdapter: SetupOrderAddressesAdapter
    private var setupOrderSelectedAddress: Address? = null

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
        showCustomBottomSheetDialogAndSaveAddress()
        onAddressRecyclerViewClick()
        deleteAddress()
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

    private fun deleteAddress() {
        setupOrderAddressesAdapter.onDeleteImageViewClick { takenAddress ->
            showAlertDialogToDeleteAddress(takenAddress)
        }
    }

    private fun showAlertDialogToDeleteAddress(address: Address) {
        val typedTitleFloatValue = TypedValue().also {
            requireContext().resources.getValue(R.dimen.customAlertDialogTitleTextViewSize, it, false)
        }.float
        val typedMessageFloatValue = TypedValue().also {
            requireContext().resources.getValue(R.dimen.customAlertDialogMessageTextViewSize, it, false)
        }.float

        showCustomAlertDialog(requireContext(),
            getString(R.string.deletingAddressAlertDialogTitleString),
            typedTitleFloatValue,
            getString(R.string.deletingAddressAlertDialogMessageString),
            typedMessageFloatValue,
            onPositiveButtonClick = {
                setupOrderViewModel.deleteAddress(address)
                setupOrderViewModel.changeSetupOrderSelectedAddressValue(null)
                observeDeleteAddressState()
            })
    }

    private fun setTotalPriceTextView() { binding.setupOrderTotalPriceTextView.text = args.cartProductsSetupOrder.totalPrice.toString().plus("$") }

    private fun closeSetupOrderFragment() { binding.closeSetupOrder.setOnClickListener { requireActivity().onBackPressed() } }

    private fun showCustomBottomSheetDialogAndSaveAddress() {
        binding.chooseAddressImageView.setOnClickListener { showBottomSheetDialog(requireContext(), onSuccess = { addedAddress -> addAddress(addedAddress) }) }
    }

    private fun onAddressRecyclerViewClick() {
        setupOrderAddressesAdapter.onRecyclerViewItemClick { address ->
            val takenAddress = address as Address
            setupOrderViewModel.changeSetupOrderSelectedAddressValue(takenAddress)
            setupOrderSelectedAddress?.let {
                binding.setupOrderButton.setOnClickListener {
                    val time = getCurrentTime()
                    val order = Order(Random().nextInt(Constants.BOUND_OF_ORDER_ID), CartProductsSetupOrder(args.cartProductsSetupOrder.cartProductList, args.cartProductsSetupOrder.totalPrice), setupOrderSelectedAddress!!, time)
                    deleteCartProductsFromCollectionAndAddSetupOrderToOrderCollection(order)
                    observeDeleteCartProductsFromCollectionAndAddSetupOrderToOrderCollection()
                    navigateToCompleteOrderFragment(order.id)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(currentTime)
    }

    private fun deleteCartProductsFromCollectionAndAddSetupOrderToOrderCollection(order: Order) {
        setupOrderViewModel.deleteAllCartProductsFromCollectionAndAddSetupOrderToOrderCollection(order)
    }

    private fun navigateToCompleteOrderFragment(orderId: Int) {
        val action = SetupOrderFragmentDirections.actionSetupOrderFragmentToCompleteOrderFragment(orderId)
        findNavController().navigate(action)
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
                launch {
                    setupOrderViewModel.setupOrderSelectedAddress.collect { takenAddress ->
                        if (takenAddress != null) {
                            hideChooseAddressWarningTextView()
                            setupOrderSelectedAddress = takenAddress
                            showSetupOrderButton()

                        } else {
                            showChooseAddressWarningTextView()
                            hideSetupOrderButton()
                        }
                    }
                }
            }
        }
    }

    private fun observeAddAddressState() {
        viewLifecycleOwner.lifecycleScope.launch {
            setupOrderViewModel.setupOrderAddedAddress.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        hideSetupOrderAddingDeletingProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_done_icon, getString(R.string.successfullyAddedAddressString))
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

    private fun observeDeleteAddressState() {
        viewLifecycleOwner.lifecycleScope.launch {
            setupOrderViewModel.setupOrderDeleteAddress.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        hideSetupOrderAddingDeletingProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_done_icon, getString(R.string.successfullyDeletedAddressString))
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

    private fun observeDeleteCartProductsFromCollectionAndAddSetupOrderToOrderCollection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    setupOrderViewModel.setupOrderDeleteCartProductsFromCollection.collect {
                        when (it) {
                            is Resource.Success -> {
                                setSetupOrderButtonStopAnimationParentViewClickableAndDarkGreyAndHideLoadingTextView()
                            }
                            is Resource.Loading -> {
                                setSetupOrderButtonStartAnimationParentViewNotClickableAndGreyAndShowLoadingTextView(getString(R.string.setupOrderLoadingTextViewDeletingCartProductsString))
                            }
                            is Resource.Error -> {
                                setSetupOrderButtonStopAnimationParentViewClickableAndDarkGreyAndHideLoadingTextView()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
                launch {
                    setupOrderViewModel.setupOrderAddToOrderCollection.collect {
                        when (it) {
                            is Resource.Success -> {
                                setSetupOrderButtonStopAnimationParentViewClickableAndDarkGreyAndHideLoadingTextView()
                            }
                            is Resource.Loading -> {
                                setSetupOrderButtonStartAnimationParentViewNotClickableAndGreyAndShowLoadingTextView(getString(R.string.setupOrderLoadingTextViewAddingOrderToCollectionString))
                            }
                            is Resource.Error -> {
                                setSetupOrderButtonStopAnimationParentViewClickableAndDarkGreyAndHideLoadingTextView()
                                showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                            }
                            is Resource.Undefined -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun showSetupOrderCartProductsProgressBar() { binding.setupOrderCartProductsProgressBar.visibility = View.VISIBLE }

    private fun hideSetupOrderCartProductsProgressBar() { binding.setupOrderCartProductsProgressBar.visibility = View.GONE }

    private fun showSetupOrderAddressesProgressBar() { binding.setupOrderAddressesProgressBar.visibility = View.VISIBLE }

    private fun hideSetupOrderAddressesProgressBar() { binding.setupOrderAddressesProgressBar.visibility = View.GONE }

    private fun showChooseAddressWarningTextView() { binding.chooseAddressWarningTextView.visibility = View.VISIBLE }

    private fun hideChooseAddressWarningTextView() { binding.chooseAddressWarningTextView.visibility = View.INVISIBLE }

    private fun showSetupOrderAddressesRecyclerView() { binding.setupOrderAddressesRecyclerView.visibility = View.VISIBLE }

    private fun hideSetupOrderAddressesRecyclerView() { binding.setupOrderAddressesRecyclerView.visibility = View.GONE }

    private fun showSetupOrderAddingDeletingProgressBar() { binding.setupOrderAddingDeletingProgressBar.visibility = View.VISIBLE }

    private fun hideSetupOrderAddingDeletingProgressBar() { binding.setupOrderAddingDeletingProgressBar.visibility = View.GONE }

    private fun showSetupOrderButton() { binding.setupOrderButton.visibility = View.VISIBLE }

    private fun hideSetupOrderButton() { binding.setupOrderButton.visibility = View.INVISIBLE }

    private fun setSetupOrderButtonStartAnimationParentViewNotClickableAndGreyAndShowLoadingTextView(loadingText: String) {
        binding.setupOrderParentView.apply {
            isClickable = false
            setBackgroundColor(resources.getColor(R.color.grey))
        }
        binding.setupOrderLoadingTextView.apply {
            visibility = View.VISIBLE
            text = loadingText
        }
        binding.setupOrderButton.startAnimation()
    }

    private fun setSetupOrderButtonStopAnimationParentViewClickableAndDarkGreyAndHideLoadingTextView() {
        binding.setupOrderParentView.apply {
            isClickable = true
            setBackgroundColor(resources.getColor(R.color.dark_grey))
        }
        binding.setupOrderLoadingTextView.visibility = View.GONE
        binding.setupOrderButton.revertAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.setupOrderButton.dispose()
    }
}