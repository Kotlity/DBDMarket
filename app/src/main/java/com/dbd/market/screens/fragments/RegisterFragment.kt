package com.dbd.market.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbd.market.R
import com.dbd.market.data.User
import com.dbd.market.databinding.FragmentRegisterBinding
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.SUCCESSFULLY_CREATED_A_NEW_ACCOUNT_TOAST_MESSAGE
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
        observeRegisterValidationEditTextsState()
        navigateToLoginFragment()
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
                        binding.appButtonRegister.revertAnimation()
                        showToast(requireActivity(), binding.root, R.drawable.ic_done_icon, SUCCESSFULLY_CREATED_A_NEW_ACCOUNT_TOAST_MESSAGE)
                        navigateToLoginFragmentAfterRegisteringANewAccount()
                    }

                    is Resource.Error -> {
                        binding.appButtonRegister.revertAnimation()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Loading -> binding.appButtonRegister.startAnimation()
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeRegisterValidationEditTextsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            registerViewModel.registerValidationState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                if (it.firstname is ValidationStatus.Error) {
                    withContext(Dispatchers.Main) {
                        binding.firstNameRegisterEditText.apply {
                            requestFocus()
                            error = it.firstname.errorMessage
                        }
                    }
                }
                if (it.lastname is ValidationStatus.Error) {
                    withContext(Dispatchers.Main) {
                        binding.lastNameRegisterEditText.apply {
                            requestFocus()
                            error = it.lastname.errorMessage
                        }
                    }
                }
                if (it.email is ValidationStatus.Error) {
                    withContext(Dispatchers.Main) {
                        binding.emailRegisterEditText.apply {
                            requestFocus()
                            error = it.email.errorMessage
                        }
                    }
                }
                if (it.password is ValidationStatus.Error) {
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

    private fun navigateToLoginFragment() {
        navigateToAnotherFragment(binding.loginLinkTextView, R.id.action_registerFragment_to_loginFragment)
    }

    private fun navigateToLoginFragmentAfterRegisteringANewAccount() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.appButtonRegister.dispose()
    }
}
