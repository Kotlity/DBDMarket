package com.dbd.market.screens.fragments.market.bottom_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.dbd.market.databinding.FragmentUserBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeFloatingActionButtonVisibleWhileCollapsingToolbar()
    }

    private fun makeFloatingActionButtonVisibleWhileCollapsingToolbar() {
        val layoutParams = binding.userTakePictureFloatingActionButton.layoutParams as CoordinatorLayout.LayoutParams
        var fabBehavior = layoutParams.behavior as? FloatingActionButton.Behavior
        fabBehavior?.let { floatingActionButtonBehavior ->
            floatingActionButtonBehavior.isAutoHideEnabled = false
        } ?: run {
            fabBehavior = FloatingActionButton.Behavior()
            fabBehavior?.isAutoHideEnabled = false
            layoutParams.behavior = fabBehavior
        }
    }

}