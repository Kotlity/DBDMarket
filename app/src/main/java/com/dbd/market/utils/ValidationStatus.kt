package com.dbd.market.utils

sealed class ValidationStatus {
    object Success: ValidationStatus()
    data class Error(val errorMessage: String): ValidationStatus()
}

data class RegisterFieldsState(val firstname: ValidationStatus, val lastname: ValidationStatus, val email: ValidationStatus, val password: ValidationStatus)
data class LoginFieldsState(val email: ValidationStatus, val password: ValidationStatus)
data class ResetPasswordFieldState(val email: ValidationStatus)
data class AddProductFieldsState(val name: ValidationStatus, val category: ValidationStatus, val description: ValidationStatus, val price: ValidationStatus, val size: ValidationStatus, val imagesList: ValidationStatus)
data class AddAddressFieldsState(
    val firstName: ValidationStatus,
    val lastName: ValidationStatus,
    val country: ValidationStatus,
    val city: ValidationStatus,
    val street: ValidationStatus,
    val type: ValidationStatus,
    val phoneNumber: ValidationStatus
)
