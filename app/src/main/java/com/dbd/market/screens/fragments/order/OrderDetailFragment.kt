package com.dbd.market.screens.fragments.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.order_detail.OrderDetailParentAdapter
import com.dbd.market.data.Address
import com.dbd.market.data.CartProduct
import com.dbd.market.databinding.FragmentOrderDetailBinding
import com.dbd.market.utils.*

class OrderDetailFragment : Fragment() {
    private val args by navArgs<OrderDetailFragmentArgs>()
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var orderDetailParentAdapter: OrderDetailParentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrderDetailToolbarTitle(args.orderDetail.id)
        closeOrderDetailFragment()
        handleOnBackPressedButton()
        setupOrderDetailAddressInformation(args.orderDetail.address)
        setupOrderDetailRecyclerView(args.orderDetail.cartProductsSetupOrder.cartProductList)
    }

    private fun setupOrderDetailToolbarTitle(orderDetailId: Int) { binding.orderDetailIdTitleTextView.text = binding.orderDetailIdTitleTextView.text.toString().plus(orderDetailId.toString()) }

    private fun closeOrderDetailFragment() { navigateToAnotherFragmentWithoutArguments(binding.closeOrderDetail, R.id.action_orderDetailFragment_to_ordersFragment) }

    private fun handleOnBackPressedButton() { onBackButtonPressed(R.id.action_orderDetailFragment_to_ordersFragment) }

    private fun setupOrderDetailAddressInformation(address: Address) {
        val orderDetailTypeString = address.type
        val updatedOrderDetailTypeString = StringBuilder(orderDetailTypeString).insert(0, "\"").insert(orderDetailTypeString.length + 1, "\"").toString()
        val orderDetailPhoneNumberString = address.phoneNumber
        val updatedOrderDetailPhoneNumberString = StringBuilder(orderDetailPhoneNumberString).insert(0, "\"").insert(orderDetailPhoneNumberString.length + 1, "\"").toString()

        binding.apply {
            orderDetailAddressTypeTextView.text = updatedOrderDetailTypeString
            orderDetailTimeTextView.text = convertDateToString(args.orderDetail.time)
            orderDetailAddressCountryTextView.text = address.country
            orderDetailAddressCityTextView.text = address.city
            orderDetailAddressStreetTextView.text = address.street
            orderDetailAddressFirstNameTextView.text = address.firstName
            orderDetailAddressLastNameTextView.text = address.lastName
            orderDetailAddressPhoneNumberTextView.text = updatedOrderDetailPhoneNumberString

            if (args.orderDetail.cartProductsSetupOrder.cartProductList.size == 1) orderDetailTitleRecyclerViewTextView.text = resources.getString(R.string.orderDetailProductTitleTextViewString)
            else orderDetailTitleRecyclerViewTextView.text = resources.getString(R.string.orderDetailProductsTitleTextViewString)

            orderDetailTotalPriceTextView.text = args.orderDetail.cartProductsSetupOrder.totalPrice.toString().plus("$")
        }
    }

    private fun setupOrderDetailRecyclerView(orderDetailRecyclerViewItemsList: List<CartProduct>) {
        orderDetailParentAdapter = OrderDetailParentAdapter(requireContext())
        binding.orderDetailRecyclerView.apply {
            adapter = orderDetailParentAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.CARTPRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
        orderDetailParentAdapter.differ.submitList(orderDetailRecyclerViewItemsList)
    }
}