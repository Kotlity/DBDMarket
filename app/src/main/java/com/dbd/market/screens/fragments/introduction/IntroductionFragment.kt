package com.dbd.market.screens.fragments.introduction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dbd.market.R
import com.dbd.market.databinding.FragmentIntroductionBinding
import com.dbd.market.utils.navigateToAnotherFragment


class IntroductionFragment : Fragment() {
    private lateinit var binding: FragmentIntroductionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToSignInUpOptionFragment()
    }

    private fun navigateToSignInUpOptionFragment() {
        navigateToAnotherFragment(binding.appButtonStart, R.id.action_introductionFragment_to_signInUpOptionFragment)
    }

}