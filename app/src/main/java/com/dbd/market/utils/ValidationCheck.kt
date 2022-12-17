package com.dbd.market.utils

import android.telephony.PhoneNumberUtils
import android.util.Patterns
import com.dbd.market.utils.Constants.CITY_IS_EMPTY
import com.dbd.market.utils.Constants.CITY_STARTS_WITH_DIGIT
import com.dbd.market.utils.Constants.CITY_STARTS_WITH_LOWERCASE
import com.dbd.market.utils.Constants.COUNTRY_IS_EMPTY
import com.dbd.market.utils.Constants.COUNTRY_STARTS_WITH_DIGIT
import com.dbd.market.utils.Constants.COUNTRY_STARTS_WITH_LOWERCASE
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
import com.dbd.market.utils.Constants.PHONE_NUMBER_IS_EMPTY
import com.dbd.market.utils.Constants.STREET_IS_EMPTY
import com.dbd.market.utils.Constants.STREET_STARTS_WITH_DIGIT
import com.dbd.market.utils.Constants.STREET_STARTS_WITH_LOWERCASE
import com.dbd.market.utils.Constants.TYPE_CONTAINS_DIGITS
import com.dbd.market.utils.Constants.TYPE_IS_EMPTY
import com.dbd.market.utils.Constants.TYPE_STARTS_WITH_LOWERCASE
import com.dbd.market.utils.Constants.WRONG_EMAIL_FORMAT
import com.dbd.market.utils.Constants.WRONG_PASSWORD_FORMAT
import com.dbd.market.utils.Constants.WRONG_PHONE_NUMBER_FORMAT
import com.dbd.market.utils.Constants.WRONG_STREET_FORMAT

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

fun checkValidationCountry(country: String): ValidationStatus {
    return if (country.isEmpty()) ValidationStatus.Error(COUNTRY_IS_EMPTY)
    else if (country.isNotEmpty() && country.first().isDigit()) ValidationStatus.Error(COUNTRY_STARTS_WITH_DIGIT)
    else if (country.isNotEmpty() && country.first().isLowerCase()) ValidationStatus.Error(COUNTRY_STARTS_WITH_LOWERCASE)
    else ValidationStatus.Success
}

fun checkValidationCity(city: String): ValidationStatus {
    return if (city.isEmpty()) ValidationStatus.Error(CITY_IS_EMPTY)
    else if (city.isNotEmpty() && city.first().isDigit()) ValidationStatus.Error(CITY_STARTS_WITH_DIGIT)
    else if (city.isNotEmpty() && city.first().isLowerCase()) ValidationStatus.Error(CITY_STARTS_WITH_LOWERCASE)
    else ValidationStatus.Success
}

fun checkValidationStreet(street: String): ValidationStatus {
    return if (street.isEmpty()) ValidationStatus.Error(STREET_IS_EMPTY)
    else if (street.isNotEmpty() && street.first().isDigit()) ValidationStatus.Error(STREET_STARTS_WITH_DIGIT)
    else if (street.isNotEmpty() && street.first().isLowerCase()) ValidationStatus.Error(STREET_STARTS_WITH_LOWERCASE)
    else if (street.matches(Regex("/[!@\$%^&*(),?\":{}|<>]/g"))) ValidationStatus.Error(WRONG_STREET_FORMAT)
    else ValidationStatus.Success
}

fun checkValidationType(type: String): ValidationStatus {
    return if (type.isEmpty()) ValidationStatus.Error(TYPE_IS_EMPTY)
    else if (type.isNotEmpty() && type.contains(Regex("[0-9]"))) ValidationStatus.Error(TYPE_CONTAINS_DIGITS)
    else if (type.isNotEmpty() && type.first().isLowerCase()) ValidationStatus.Error(TYPE_STARTS_WITH_LOWERCASE)
    else ValidationStatus.Success
}

fun checkValidationPhoneNumber(phoneNumber: String): ValidationStatus {
    return if (phoneNumber.isEmpty()) ValidationStatus.Error(PHONE_NUMBER_IS_EMPTY)
    else if (!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) ValidationStatus.Error(WRONG_PHONE_NUMBER_FORMAT)
    else ValidationStatus.Success
}