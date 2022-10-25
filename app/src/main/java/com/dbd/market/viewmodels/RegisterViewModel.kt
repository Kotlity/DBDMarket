package com.dbd.market.viewmodels

import com.dbd.market.data.User
import com.dbd.market.repositories.RegisterRepository
import com.dbd.market.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository) {

    private var _registerUser = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val registerUser = _registerUser.asStateFlow()

    fun createUserWithEmailAndPassword(user: User, password: String) {
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