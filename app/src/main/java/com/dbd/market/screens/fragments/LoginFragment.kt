package com.dbd.market.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.dbd.market.R
import com.dbd.market.databinding.FragmentLoginBinding
import com.dbd.market.utils.Constants.LOGCAT_TAG
import com.dbd.market.utils.LoginRegisterValidation
import com.dbd.market.utils.Resource
import com.dbd.market.utils.setUnderlineToLinkTextView
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUnderlineToLinkTextView(getString(R.string.forgotPasswordString), binding.forgotPasswordLinkTextView)
        loginUserByEmailAndPassword()
        observeLoginState()
        observeLoginValidationEditTextsState()
    }

    private fun loginUserByEmailAndPassword() {
        binding.apply {
            appButtonLogin.setOnClickListener {
                val email = emailLoginEditText.text.toString().trim()
                val password = passwordLoginEditText.text.toString().trim()
                loginViewModel.loginUserWithEmailAndPassword(email, password)
            }
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.loginUser.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        Log.d(LOGCAT_TAG,it.data.toString())
                        binding.appButtonLogin.revertAnimation()
                    }
                    is Resource.Error -> {
                        showToast(requireContext(), binding.root, it.message.toString())
                        binding.appButtonLogin.revertAnimation()
                    }
                    is Resource.Loading -> binding.appButtonLogin.startAnimation()
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeLoginValidationEditTextsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.loginValidationState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                if (it.email is LoginRegisterValidation.Error) {
                    withContext(Dispatchers.Main) {
                        binding.emailLoginEditText.apply {
                            requestFocus()
                            error = it.email.errorMessage
                        }
                    }
                }
                if (it.password is LoginRegisterValidation.Error) {
                    withContext(Dispatchers.Main) {
                        binding.passwordLoginEditText.apply {
                            requestFocus()
                            error = it.password.errorMessage
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.appButtonLogin.dispose()
    }
}