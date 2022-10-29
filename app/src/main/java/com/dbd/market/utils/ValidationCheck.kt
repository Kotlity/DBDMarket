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

fun checkValidationFirstname(firstname: String): LoginRegisterValidation {
    return if (firstname.isEmpty()) LoginRegisterValidation.Error(FIRSTNAME_IS_EMPTY)
    else if (firstname.isNotEmpty() && firstname.first().isDigit()) LoginRegisterValidation.Error(FIRSTNAME_STARTS_WITH_DIGIT)
    else if (firstname.isNotEmpty() && firstname.first().isLowerCase()) LoginRegisterValidation.Error(FIRSTNAME_STARTS_WITH_LOWERCASE)
    else LoginRegisterValidation.Success
}

fun checkValidationLastname(lastname: String): LoginRegisterValidation {
    return if (lastname.isEmpty()) LoginRegisterValidation.Error(LASTNAME_IS_EMPTY)
    else if (lastname.isNotEmpty() && lastname.first().isDigit()) LoginRegisterValidation.Error(LASTNAME_STARTS_WITH_DIGIT)
    else if (lastname.isNotEmpty() && lastname.first().isLowerCase()) LoginRegisterValidation.Error(LASTNAME_STARTS_WITH_LOWERCASE)
    else LoginRegisterValidation.Success
}

fun checkValidationEmail(email: String): LoginRegisterValidation {
    return if (email.isEmpty()) LoginRegisterValidation.Error(EMAIL_IS_EMPTY)
    else if (email.isNotEmpty() && email.first().isUpperCase()) LoginRegisterValidation.Error(EMAIL_STARTS_WITH_UPPERCASE)
    else if (email.isNotEmpty() && email.first().isDigit()) LoginRegisterValidation.Error(EMAIL_STARTS_WITH_DIGIT)
    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) LoginRegisterValidation.Error(WRONG_EMAIL_FORMAT)
    else LoginRegisterValidation.Success
}

fun checkValidationPassword(password: String): LoginRegisterValidation {
    return if (password.isEmpty()) LoginRegisterValidation.Error(PASSWORD_IS_EMPTY)
    else if(password.isNotEmpty() && password.first().isDigit()) LoginRegisterValidation.Error(WRONG_PASSWORD_FORMAT)
    else if(password.length < MIN_PASSWORD_LENGTH) LoginRegisterValidation.Error(MINIMUM_PASSWORD_LENGTH)
    else LoginRegisterValidation.Success
}