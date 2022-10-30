package com.dbd.market.utils

object Constants {

    const val LOGCAT_TAG = "MyTag"

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

    const val USER_COLLECTION_PATH = "user"

    const val SUCCESSFULLY_ACCOUNT_LOGIN_TOAST_MESSAGE = "You have successfully logged in"
    const val SUCCESSFULLY_CREATED_A_NEW_ACCOUNT_TOAST_MESSAGE = "You have successfully created a new account, now sign in"
}