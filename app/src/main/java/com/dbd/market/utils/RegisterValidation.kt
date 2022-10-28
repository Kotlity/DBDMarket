package com.dbd.market.utils

sealed class RegisterValidation {
    object Success: RegisterValidation()
    data class Error(val errorMessage: String): RegisterValidation()
}

data class RegisterFieldsState(val firstname: RegisterValidation, val lastname: RegisterValidation, val email: RegisterValidation, val password: RegisterValidation)
