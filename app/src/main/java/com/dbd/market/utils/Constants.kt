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

    const val PRODUCT_NAME_IS_EMPTY = "The product's name cannot be empty"
    const val PRODUCT_CATEGORY_IS_EMPTY = "The product's category cannot be empty"
    const val PRODUCT_DESCRIPTION_IS_EMPTY = "The product's description cannot be empty"
    const val PRODUCT_PRICE_IS_EMPTY = "The product's price cannot be empty"
    const val PRODUCT_SIZE_IS_EMPTY = "The product's size cannot be empty"

    const val PRODUCT_NAME_MAX_LENGTH = 50
    const val PRODUCT_CATEGORY_MAX_LENGTH = 40
    const val PRODUCT_DESCRIPTION_MAX_LENGTH = 200
    const val PRODUCT_PRICE_MAX_LENGTH = 5
    const val PRODUCT_DISCOUNT_MAX_LENGTH = 4
    const val PRODUCT_SIZE_MAX_LENGTH = 4

    const val PRODUCT_DISCOUNT_MAX_VALUE = 1.0

    const val PRODUCT_NAME_WRONG_LENGTH = "The product's name cannot be more than 50 characters"
    const val PRODUCT_CATEGORY_WRONG_LENGTH = "The product's category cannot be more than 40 characters"
    const val PRODUCT_DESCRIPTION_WRONG_LENGTH = "The product's description cannot be more than 200 characters"
    const val PRODUCT_PRICE_WRONG_LENGTH = "The product's price cannot be more than 99999$"
    const val PRODUCT_DISCOUNT_WRONG_LENGTH = "The product's discount cannot be more than 4 characters"
    const val PRODUCT_SIZE_WRONG_LENGTH = "The product's size cannot be more than 15 characters"

    const val WRONG_PRODUCT_CATEGORY_FORMAT = "Wrong format of the product's category"
    const val PRODUCT_CATEGORY_CONTAINS_DIGITS = "The product's category cannot contains digits"
    const val PRODUCT_SIZE_CONTAINS_DIGITS = "The product's size cannot contains digits"
    const val PRODUCT_SIZE_CONTAINS_LOWERCASE = "The product's size cannot contains lowercase"
    const val PRODUCT_DISCOUNT_WRONG_MAX_VALUE = "The product's discount cannot be more than 0.99 percent"
    const val PRODUCT_PRICE_CONTAINS_SPACE = "The product's price cannot contains spaces"
    const val PRODUCT_PRICE_CONTAINS_WRONG_INPUT = "The product's price should contains only digits"
    const val PRODUCT_DISCOUNT_CONTAINS_SPACE = "The product's discount cannot contains spaces"
    const val PRODUCT_DISCOUNT_WRONG_FIRST_CHAR = "The product's discount should starts with 0"
    const val PRODUCT_DISCOUNT_WRONG_SECOND_CHAR = "The product's discount should contains . after 0"

    const val PRODUCT_NAME_STARTS_WITH_LOWERCASE = "The product's name cannot starts with lowercase"
    const val PRODUCT_DESCRIPTION_STARTS_WITH_LOWERCASE = "The product's description cannot starts with lowercase"

    const val PRODUCT_NAME_STARTS_WITH_DIGIT = "The product's name cannot starts with a digit"
    const val PRODUCT_DESCRIPTION_STARTS_WITH_DIGIT = "The product's description cannot starts with a digit"
}