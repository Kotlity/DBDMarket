package com.dbd.market.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.repositories.login.LoginRepository
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

    fun loginUserWithEmailAndPassword(email: String, password: String) {
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
    }
}