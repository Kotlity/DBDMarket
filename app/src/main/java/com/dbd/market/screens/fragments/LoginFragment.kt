package com.dbd.market.screens.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dbd.market.R
import com.dbd.market.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUnderlineToLinksTextViews()
    }

    private fun setUnderlineToLinksTextViews() {
        val forgotPasswordCharSequence = getString(R.string.forgotPasswordString)
        val registerCharSequence = getString(R.string.registerString)
        val forgotPasswordSpannableString = SpannableString(forgotPasswordCharSequence)
        val registerSpannableString = SpannableString(registerCharSequence)
        forgotPasswordSpannableString.setSpan(UnderlineSpan(), resources.getInteger(R.integer.zero), forgotPasswordCharSequence.length, resources.getInteger(R.integer.zero))
        registerSpannableString.setSpan(UnderlineSpan(), resources.getInteger(R.integer.zero), registerCharSequence.length, resources.getInteger(R.integer.zero))
        binding.forgotPasswordLinkTextView.text = forgotPasswordSpannableString
        binding.registerLinkTextView.text = registerSpannableString
    }

}