package com.dbd.market.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.User
import com.dbd.market.repositories.RegisterRepository
import com.dbd.market.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository): ViewModel() {

    private var _registerUser = MutableStateFlow<Resource<FirebaseUser>>(Resource.Undefined())
    val registerUser = _registerUser.asStateFlow()

    fun createUserWithEmailAndPassword(user: User, password: String) {
        viewModelScope.launch {
            _registerUser.emit(Resource.Loading())
        }
        repository.createUserWithEmailAndPassword(user, password,
            onSuccess = { authResult ->
                authResult.user?.let { firebaseUser ->
                    _registerUser.value = Resource.Success(firebaseUser)
                }
            },
            onFailure = { authException ->
                _registerUser.value = Resource.Error(authException.message.toString())
            }
        )
    }
}