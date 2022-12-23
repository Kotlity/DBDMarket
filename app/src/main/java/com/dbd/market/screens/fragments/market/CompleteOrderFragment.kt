package com.dbd.market.screens.fragments.market

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dbd.market.R
import com.dbd.market.databinding.FragmentCompleteOrderBinding
import com.dbd.market.utils.navigateToAnotherFragmentWithoutArguments
import com.dbd.market.utils.onBackButtonPressed

class CompleteOrderFragment : Fragment() {
    private lateinit var binding: FragmentCompleteOrderBinding
    private val args by navArgs<CompleteOrderFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteOrderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCompleteOrderIdTextView(args.completeOrderId)
        navigateToOrdersFragment()
        navigateToHomeFragment()
        handleOnBackPressedButton()
    }

    private fun setupCompleteOrderIdTextView(orderId: Int) { binding.completeOrderIdTextView.text = orderId.toString() }

    private fun navigateToHomeFragment() { navigateToAnotherFragmentWithoutArguments(binding.closeCompleteOrder, R.id.action_completeOrderFragment_to_homeFragment) }

    private fun handleOnBackPressedButton() { onBackButtonPressed(R.id.action_completeOrderFragment_to_homeFragment) }

    private fun navigateToOrdersFragment() { navigateToAnotherFragmentWithoutArguments(binding.orderDetailsButton, R.id.action_completeOrderFragment_to_ordersFragment) }

}