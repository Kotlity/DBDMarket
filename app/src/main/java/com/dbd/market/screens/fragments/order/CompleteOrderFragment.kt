package com.dbd.market.screens.fragments.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dbd.market.R
import com.dbd.market.data.Order
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
        setupCompleteOrderIdTextView(args.orderArgument.id)
        navigateToOrderDetailFragment(args.orderArgument)
        navigateToHomeFragment()
        handleOnBackPressedButton()
    }

    private fun setupCompleteOrderIdTextView(orderId: Int) { binding.completeOrderIdTextView.text = orderId.toString() }

    private fun navigateToHomeFragment() { navigateToAnotherFragmentWithoutArguments(binding.closeCompleteOrder, R.id.action_completeOrderFragment_to_homeFragment) }

    private fun handleOnBackPressedButton() { onBackButtonPressed(R.id.action_completeOrderFragment_to_homeFragment) }

    private fun navigateToOrderDetailFragment(order: Order) {
        binding.orderDetailButton.setOnClickListener {
            val action = CompleteOrderFragmentDirections.actionCompleteOrderFragmentToOrderDetailFragment(order)
            findNavController().navigate(action)
        }
    }

}