package com.dbd.market.utils

sealed class LoginRegisterValidation {
    object Success: LoginRegisterValidation()
    data class Error(val errorMessage: String): LoginRegisterValidation()
}

data class RegisterFieldsState(val firstname: LoginRegisterValidation, val lastname: LoginRegisterValidation, val email: LoginRegisterValidation, val password: LoginRegisterValidation)
data class LoginFieldsState(val email: LoginRegisterValidation, val password: LoginRegisterValidation)
