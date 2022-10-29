package com.dbd.market.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.User
import com.dbd.market.repositories.register.RegisterRepository
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.USER_COLLECTION_PATH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository): ViewModel() {

    private val _registerUser = MutableStateFlow<Resource<User>>(Resource.Undefined())
    val registerUser = _registerUser.asStateFlow()

    private val _registerValidationState = Channel<RegisterFieldsState>()
    val registerValidationState = _registerValidationState.receiveAsFlow()

    fun createUserWithEmailAndPassword(user: User, password: String) {
        if (isCorrectedEditTextsInput(user, password)) {
            viewModelScope.launch {
                _registerUser.emit(Resource.Loading())
            }
            registerRepository.createUserWithEmailAndPassword(user, password,
                onSuccess = { authResult ->
                    authResult.user?.let { firebaseUser ->
                        saveUserInfoToFirebaseFirestore(firebaseUser.uid, user, USER_COLLECTION_PATH)
                    }
                },
                onFailure = { authException ->
                    _registerUser.value = Resource.Error(authException.message.toString())
                }
            )
        } else {
            val registerFieldsState = RegisterFieldsState(
                checkValidationFirstname(user.firstName),
                checkValidationLastname(user.lastName),
                checkValidationEmail(user.email),
                checkValidationPassword(password))
            viewModelScope.launch {
                _registerValidationState.send(registerFieldsState)
            }
        }
    }

    private fun isCorrectedEditTextsInput(user: User, password: String): Boolean {
        val validationFirstname = checkValidationFirstname(user.firstName)
        val validationLastname = checkValidationLastname(user.lastName)
        val validationEmail = checkValidationEmail(user.email)
        val validationPassword = checkValidationPassword(password)
        return (validationFirstname is LoginRegisterValidation.Success
                && validationLastname is LoginRegisterValidation.Success
                && validationEmail is LoginRegisterValidation.Success
                && validationPassword is LoginRegisterValidation.Success)
    }

    private fun saveUserInfoToFirebaseFirestore(userUID: String, user: User, collectionPath: String) {
        registerRepository.saveUserToFirebaseFirestore(userUID, user, collectionPath,
            onSuccess = {
                _registerUser.value = Resource.Success(user)
            },
            onFailure = { savingException ->
                _registerUser.value = Resource.Error(savingException.message.toString())
            }
        )
    }
}