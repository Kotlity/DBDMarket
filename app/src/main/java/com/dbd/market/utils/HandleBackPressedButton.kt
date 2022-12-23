package com.dbd.market.utils

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.onBackButtonPressed(destination: Int) {
    requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigate(destination)
        }
    })
}