package com.dbd.market.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigateToAnotherFragment(view: View, destination: Int) {
    view.setOnClickListener {
        findNavController().navigate(destination)
    }
}