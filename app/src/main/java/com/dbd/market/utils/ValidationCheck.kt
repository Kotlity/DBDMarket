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

fun checkValidationFirstname(firstname: String): ValidationStatus {
    return if (firstname.isEmpty()) ValidationStatus.Error(FIRSTNAME_IS_EMPTY)
    else if (firstname.isNotEmpty() && firstname.first().isDigit()) ValidationStatus.Error(FIRSTNAME_STARTS_WITH_DIGIT)
    else if (firstname.isNotEmpty() && firstname.first().isLowerCase()) ValidationStatus.Error(FIRSTNAME_STARTS_WITH_LOWERCASE)
    else ValidationStatus.Success
}

fun checkValidationLastname(lastname: String): ValidationStatus {
    return if (lastname.isEmpty()) ValidationStatus.Error(LASTNAME_IS_EMPTY)
    else if (lastname.isNotEmpty() && lastname.first().isDigit()) ValidationStatus.Error(LASTNAME_STARTS_WITH_DIGIT)
    else if (lastname.isNotEmpty() && lastname.first().isLowerCase()) ValidationStatus.Error(LASTNAME_STARTS_WITH_LOWERCASE)
    else ValidationStatus.Success
}

fun checkValidationEmail(email: String): ValidationStatus {
    return if (email.isEmpty()) ValidationStatus.Error(EMAIL_IS_EMPTY)
    else if (email.isNotEmpty() && email.first().isUpperCase()) ValidationStatus.Error(EMAIL_STARTS_WITH_UPPERCASE)
    else if (email.isNotEmpty() && email.first().isDigit()) ValidationStatus.Error(EMAIL_STARTS_WITH_DIGIT)
    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) ValidationStatus.Error(WRONG_EMAIL_FORMAT)
    else ValidationStatus.Success
}

fun checkValidationPassword(password: String): ValidationStatus {
    return if (password.isEmpty()) ValidationStatus.Error(PASSWORD_IS_EMPTY)
    else if(password.isNotEmpty() && password.first().isDigit()) ValidationStatus.Error(WRONG_PASSWORD_FORMAT)
    else if(password.length < MIN_PASSWORD_LENGTH) ValidationStatus.Error(MINIMUM_PASSWORD_LENGTH)
    else ValidationStatus.Success
}