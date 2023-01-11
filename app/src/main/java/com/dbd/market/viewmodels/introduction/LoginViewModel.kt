package com.dbd.market.viewmodels.introduction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.User
import com.dbd.market.helpers.operations.UserResettingPasswordOperation
import com.dbd.market.repositories.introduction.login.LoginRepository
import com.dbd.market.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userResettingPasswordOperation: UserResettingPasswordOperation): ViewModel() {

    private val _loginUser: MutableSharedFlow<Resource<FirebaseUser>> = MutableSharedFlow()
    val loginUser = _loginUser.asSharedFlow()

    private val _loginValidationState = Channel<LoginFieldsState>()
    val loginValidationState = _loginValidationState.receiveAsFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    private val _loginUserWithGoogle = MutableSharedFlow<Resource<Boolean>>()
    val loginUserWithGoogle = _loginUserWithGoogle.asSharedFlow()

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
        userResettingPasswordOperation.resetUserPasswordViaEmail(email,
            onSuccess = {
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Success(email))
                }
            },
            onFailure = { resetException ->
                viewModelScope.launch {
                    _resetPassword.emit(Resource.Error(resetException))
                }
            }
        )
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginUserWithGoogle.emit(Resource.Loading())
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            loginRepository.signInWithGoogle(credential).addOnSuccessListener {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                val email = firebaseUser!!.email!!
                val image = firebaseUser.photoUrl?.toString()
                val user = User("", "", email, image ?: "")
                loginRepository.saveUserInformationToFirebaseFirestore(firebaseUser.uid, user, onSuccess = {
                    viewModelScope.launch(Dispatchers.IO) {
                        _loginUserWithGoogle.emit(Resource.Success(true))
                    }
                }, onFailure = { savingUserInformationToFirebaseFirestoreError ->
                    viewModelScope.launch(Dispatchers.IO) {
                        _loginUserWithGoogle.emit(Resource.Error(savingUserInformationToFirebaseFirestoreError))
                    }
                })
            }.addOnFailureListener { signingWithGoogleError -> {
                viewModelScope.launch(Dispatchers.IO) { _loginUserWithGoogle.emit(Resource.Error(signingWithGoogleError.message.toString())) }
            }
            }
        }
    }
}