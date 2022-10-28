package com.dbd.market.utils

import android.util.Patterns

const val FIRSTNAME_IS_EMPTY= "Firstname cannot be empty"
const val FIRSTNAME_STARTS_WITH_LOWERCASE = "Firstname cannot starts with lowercase"
const val FIRSTNAME_STARTS_WITH_DIGIT = "Firstname cannot starts with a digit"
const val LASTNAME_IS_EMPTY= "Lastname cannot be empty"
const val LASTNAME_STARTS_WITH_LOWERCASE = "Lastname cannot starts with lowercase"
const val LASTNAME_STARTS_WITH_DIGIT = "Lastname cannot starts with a digit"
const val EMAIL_IS_EMPTY = "Email cannot be empty"
const val WRONG_EMAIL_FORMAT = "Wrong email format"
const val EMAIL_STARTS_WITH_DIGIT = "Email cannot starts with a digit"
const val EMAIL_STARTS_WITH_UPPERCASE = "Email cannot starts with uppercase"
const val PASSWORD_IS_EMPTY = "Password cannot be empty"
const val WRONG_PASSWORD_FORMAT = "Password cannot starts with a digit"
const val MINIMUM_PASSWORD_LENGTH = "Password must not be less than 6 characters"
const val MIN_PASSWORD_LENGTH = 6

fun checkValidationFirstname(firstname: String): RegisterValidation {
    return if (firstname.isEmpty()) RegisterValidation.Error(FIRSTNAME_IS_EMPTY)
    else if (firstname.isNotEmpty() && firstname.first().isDigit()) RegisterValidation.Error(FIRSTNAME_STARTS_WITH_DIGIT)
    else if (firstname.isNotEmpty() && firstname.first().isLowerCase()) RegisterValidation.Error(FIRSTNAME_STARTS_WITH_LOWERCASE)
    else RegisterValidation.Success
}

fun checkValidationLastname(lastname: String): RegisterValidation {
    return if (lastname.isEmpty()) RegisterValidation.Error(LASTNAME_IS_EMPTY)
    else if (lastname.isNotEmpty() && lastname.first().isDigit()) RegisterValidation.Error(LASTNAME_STARTS_WITH_DIGIT)
    else if (lastname.isNotEmpty() && lastname.first().isLowerCase()) RegisterValidation.Error(LASTNAME_STARTS_WITH_LOWERCASE)
    else RegisterValidation.Success
}

fun checkValidationEmail(email: String): RegisterValidation {
    return if (email.isEmpty()) RegisterValidation.Error(EMAIL_IS_EMPTY)
    else if (email.isNotEmpty() && email.first().isUpperCase()) RegisterValidation.Error(EMAIL_STARTS_WITH_UPPERCASE)
    else if (email.isNotEmpty() && email.first().isDigit()) RegisterValidation.Error(EMAIL_STARTS_WITH_DIGIT)
    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) RegisterValidation.Error(WRONG_EMAIL_FORMAT)
    else RegisterValidation.Success
}

fun checkValidationPassword(password: String): RegisterValidation {
    return if (password.isEmpty()) RegisterValidation.Error(PASSWORD_IS_EMPTY)
    else if(password.isNotEmpty() && password.first().isDigit()) RegisterValidation.Error(WRONG_PASSWORD_FORMAT)
    else if(password.length < MIN_PASSWORD_LENGTH) RegisterValidation.Error(MINIMUM_PASSWORD_LENGTH)
    else RegisterValidation.Success
}