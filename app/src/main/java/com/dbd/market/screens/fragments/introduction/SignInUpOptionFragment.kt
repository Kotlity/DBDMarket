package com.dbd.market.screens.fragments.introduction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dbd.market.R
import com.dbd.market.databinding.FragmentSignInUpOptionBinding
import com.dbd.market.utils.navigateToAnotherFragmentWithoutArguments

class SignInUpOptionFragment : Fragment() {
    private lateinit var binding: FragmentSignInUpOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInUpOptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToLoginFragment()
        navigateToRegisterFragment()
    }

    private fun navigateToLoginFragment() {
        navigateToAnotherFragmentWithoutArguments(binding.appButtonSignIn, R.id.action_signInUpOptionFragment_to_loginFragment)
    }

    private fun navigateToRegisterFragment() {
        navigateToAnotherFragmentWithoutArguments(binding.appButtonSignUp, R.id.action_signInUpOptionFragment_to_registerFragment)
    }

}