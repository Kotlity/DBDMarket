package com.dbd.market.screens.fragments.market.categories

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dbd.market.databinding.FragmentMainCategoryBinding

class MainCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProgressBarColor()
    }

    private fun setupProgressBarColor() {
        binding.mainCategoryProgressBar.progressTintList = ColorStateList.valueOf(Color.RED)
    }
}