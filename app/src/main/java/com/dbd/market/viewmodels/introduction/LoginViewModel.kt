package com.dbd.market.viewmodels.introduction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.repositories.introduction.login.LoginRepository
import com.dbd.market.utils.*
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository): ViewModel() {

    private val _loginUser: MutableSharedFlow<Resource<FirebaseUser>> = MutableSharedFlow()
    val loginUser = _loginUser.asSharedFlow()

    private val _loginValidationState = Channel<LoginFieldsState>()
    val loginValidationState = _loginValidationState.receiveAsFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun loginUserWithEmailAndPassword(email: String, password: String) {
        if (isCorrectedEditTextsInput(email, password)) {
            viewModelScope.launch {
                _loginUser.emit(Resource.Loading())
            }
            loginRepository.loginUserWithEmailAndPassword(email, password,
                onSuccess = { authResult ->
                    authResult.user?.let { firebaseUser ->
                        viewModelScope.launch {
                            _loginUser.emit(Resource.Success(firebaseUser))
                        }
                    }
                },
                onFailure = { authException ->
                    viewModelScope.launch {
                        _loginUser.emit(Resource.Error(authException.message.toString()))
                    }
                }
            )
        } else {
            val loginFieldsState = LoginFieldsState(
                email = checkValidationEmail(email),
                password = checkValidationPassword(password)
            )
            viewModelScope.launch {
                _loginValidationState.send(loginFieldsState)
            }
        }
    }

    private fun isCorrectedEditTextsInput(email: String, password: String): Boolean {
        val emailValidation = checkValidationEmail(email)
        val passwordValidation = checkValidationPassword(password)
        return emailValidation is ValidationStatus.Success && passwordValidation is ValidationStatus.Success
    }

    fun resetPasswordViaEmail(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
        loginRepository.resetPasswordWithEmail(email,
            onSuccess = {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Success(email))
                }
            },
            onFailure = { resetException ->
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Error(resetException.message.toString()))
                }
            }
        )
    }
}