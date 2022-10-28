package com.dbd.market.utils

import android.util.Patterns
import com.dbd.market.utils.Constants.EMAIL_IS_EMPTY
import com.dbd.market.utils.Constants.EMAIL_STARTS_WITH_DIGIT
import com.dbd.market.utils.Constants.EMAIL_STARTS_WITH_UPPERCASE
import com.dbd.market.utils.Constants.FIRSTNAME_IS_EMPTY
import com.dbd.market.utils.Constants.FIRSTNAME_STARTS_WITH_DIGIT
import com.dbd.market.utils.Constants.FIRSTNAME_STARTS_WITH_LOWERCASE
import com.dbd.market.utils.Constants.LASTNAME_IS_EMPTY
import com.dbd.market.utils.Constants.LASTNAME_STARTS_WITH_DIGIT
import com.dbd.market.utils.Constants.LASTNAME_STARTS_WITH_LOWERCASE
import com.dbd.market.utils.Constants.MINIMUM_PASSWORD_LENGTH
import com.dbd.market.utils.Constants.MIN_PASSWORD_LENGTH
import com.dbd.market.utils.Constants.PASSWORD_IS_EMPTY
import com.dbd.market.utils.Constants.WRONG_EMAIL_FORMAT
import com.dbd.market.utils.Constants.WRONG_PASSWORD_FORMAT

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