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
import com.dbd.market.data.User
import com.dbd.market.databinding.FragmentRegisterBinding
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.LOGCAT_TAG
import com.dbd.market.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUnderlineToLinkTextView(getString(R.string.loginLinkString), binding.loginLinkTextView)
        registerUserByEmailAndPassword()
        observeRegisterState()
        observeValidationEditTextsState()
    }

    private fun registerUserByEmailAndPassword() {
        binding.apply {
            appButtonRegister.setOnClickListener {
                val firstName = firstNameRegisterEditText.text.toString().trim()
                val lastName = lastNameRegisterEditText.text.toString().trim()
                val email = emailRegisterEditText.text.toString().trim()
                val password = passwordRegisterEditText.text.toString().trim()
                val user = User(firstName, lastName, email)
                registerViewModel.createUserWithEmailAndPassword(user, password)
            }
        }
    }

    private fun observeRegisterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            registerViewModel.registerUser.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        Log.d(LOGCAT_TAG,it.data.toString())
                        binding.appButtonRegister.revertAnimation()
                    }

                    is Resource.Error -> {
                        showToast(requireContext(), binding.root)
                        binding.appButtonRegister.revertAnimation()
                    }
                    is Resource.Loading -> binding.appButtonRegister.startAnimation()
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeValidationEditTextsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            registerViewModel.validationState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                if (it.firstname is RegisterValidation.Error) {
                    withContext(Dispatchers.Main) {
                        binding.firstNameRegisterEditText.apply {
                            requestFocus()
                            error = it.firstname.errorMessage
                        }
                    }
                }
                if (it.lastname is RegisterValidation.Error) {
                    withContext(Dispatchers.Main) {
                        binding.lastNameRegisterEditText.apply {
                            requestFocus()
                            error = it.lastname.errorMessage
                        }
                    }
                }
                if (it.email is RegisterValidation.Error) {
                    withContext(Dispatchers.Main) {
                        binding.emailRegisterEditText.apply {
                            requestFocus()
                            error = it.email.errorMessage
                        }
                    }
                }
                if (it.password is RegisterValidation.Error) {
                    withContext(Dispatchers.Main) {
                        binding.passwordRegisterEditText.apply {
                            requestFocus()
                            error = it.password.errorMessage
                        }
                    }
                }
            }
        }
    }
}
