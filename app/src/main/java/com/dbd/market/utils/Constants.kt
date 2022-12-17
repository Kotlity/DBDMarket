package com.dbd.market.utils

object Constants {

    const val LOGCAT_TAG = "MyTag"

    const val REQUEST_CODE_STORAGE_PERMISSION = 1
    const val REQUEST_CODE_SELECT_IMAGES = 2

    const val MIN_AMOUNT_OF_IMAGES_TO_SHOW_RECT_VIEW_PAGER2_INDICATOR = 2

    const val PERMISSION_TITLE = "Storage"

    const val FIREBASE_FIRESTORE_PRODUCTS_COLLECTION = "Products"
    const val FIREBASE_FIRESTORE_USER_COLLECTION = "user"
    const val FIREBASE_FIRESTORE_CART_PRODUCTS_COLLECTION = "CartProducts"

    const val FIREBASE_FIRESTORE_CART_PRODUCTS_ID_FIELD = "id"
    const val FIREBASE_FIRESTORE_CART_PRODUCTS_SIZE_FIELD = "size"
    const val FIREBASE_FIRESTORE_CART_PRODUCTS_AMOUNT_FIELD = "amount"

    const val FIREBASE_FIRESTORE_PRODUCTS_CATEGORY_FIELD = "category"
    const val FIREBASE_FIRESTORE_SPECIAL_PRODUCTS_CATEGORY_FIELD_VALUE = "Special products"
    const val FIREBASE_FIRESTORE_BENEFICIAL_PRODUCTS_CATEGORY_FIELD_VALUE = "Beneficial products"
    const val FIREBASE_FIRESTORE_INTERESTING_PRODUCTS_CATEGORY_FIELD_VALUE = "Interesting products"
    const val FIREBASE_FIRESTORE_SUITS_PRODUCTS_CATEGORY_FIELD_VALUE = "Suits"
    const val FIREBASE_FIRESTORE_WEAPONS_PRODUCTS_CATEGORY_FIELD_VALUE = "Weapon"
    const val FIREBASE_FIRESTORE_HEADDRESS_PRODUCTS_CATEGORY_FIELD_VALUE = "Headdress"
    const val FIREBASE_FIRESTORE_TORSO_PRODUCTS_CATEGORY_FIELD_VALUE = "Torso"
    const val FIREBASE_FIRESTORE_LEGS_PRODUCTS_CATEGORY_FIELD_VALUE = "Legs"
    const val FIREBASE_FIRESTORE_PRODUCTS_DISCOUNT_FIELD= "discount"

    const val RECYCLER_VIEW_AUTO_SCROLL_PERIOD = 3000L
    const val PRODUCT_DESCRIPTION_IMAGE_VIEW_ANIMATION_DURATION = 400
    const val CART_PRODUCT_IMAGE_VIEW_ANIMATION_DURATION = 300

    const val PERMISSION_HAS_DENIED = "You have not allowed the app to access your storage. Please do it manually in the app settings"
    const val ALERT_DIALOG_PERMISSION_RATIONALE_TITLE = "Storage permission required"

    const val PRODUCT_SUCCESSFULLY_ADDED = "Product successfully added"

    const val DELETE_ALL_TAKEN_IMAGES_ALERT_DIALOG_TITLE = "Delete all taken images"
    const val DELETE_ALL_TAKEN_IMAGES_ALERT_DIALOG_MESSAGE = "Are you sure you want to delete all taken images?"
    const val SUCCESSFULLY_DELETED_ALL_TAKEN_IMAGES = "All taken images have successfully deleted"

    const val IMAGES_ARE_NOT_SELECTED = "Images are not selected"

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

    const val SUCCESSFULLY_ACCOUNT_LOGIN_TOAST_MESSAGE = "You have successfully logged in"
    const val SUCCESSFULLY_CREATED_A_NEW_ACCOUNT_TOAST_MESSAGE = "You have successfully created a new account, now sign in"

    const val PRODUCT_NAME_IS_EMPTY = "The product's name cannot be empty"
    const val PRODUCT_CATEGORY_IS_EMPTY = "The product's category cannot be empty"
    const val PRODUCT_DESCRIPTION_IS_EMPTY = "The product's description cannot be empty"
    const val PRODUCT_PRICE_IS_EMPTY = "The product's price cannot be empty"
    const val PRODUCT_SIZE_IS_EMPTY = "The product's size cannot be empty"
    const val PRODUCT_IMAGE_IS_EMPTY = "At least 1 product photo is required"

    const val PRODUCT_NAME_MAX_LENGTH = 50
    const val PRODUCT_DESCRIPTION_MAX_LENGTH = 300
    const val PRODUCT_PRICE_MAX_LENGTH = 3
    const val PRODUCT_SIZE_MAX_LENGTH = 4

    const val PRODUCT_NAME_WRONG_LENGTH = "The product's name cannot be more than 50 characters"
    const val PRODUCT_DESCRIPTION_WRONG_LENGTH = "The product's description cannot be more than 300 characters"
    const val PRODUCT_PRICE_WRONG_LENGTH = "The product's price cannot be more than 999$"
    const val PRODUCT_SIZE_WRONG_LENGTH = "The product's size cannot be more than 15 characters"

    const val WRONG_PRODUCT_CATEGORY_FORMAT = "Wrong format of the product's category"
    const val PRODUCT_CATEGORY_CONTAINS_DIGITS = "The product's category cannot contains digits"
    const val PRODUCT_SIZE_CONTAINS_DIGITS = "The product's size cannot contains digits"
    const val PRODUCT_SIZE_CONTAINS_LOWERCASE = "The product's size cannot contains lowercase"
    const val PRODUCT_PRICE_CONTAINS_SPACE = "The product's price cannot contains spaces"
    const val PRODUCT_PRICE_CONTAINS_WRONG_INPUT = "The product's price should contains only digits"

    const val PRODUCT_NAME_STARTS_WITH_LOWERCASE = "The product's name cannot starts with lowercase"
    const val PRODUCT_DESCRIPTION_STARTS_WITH_LOWERCASE = "The product's description cannot starts with lowercase"

    const val PRODUCT_NAME_STARTS_WITH_DIGIT = "The product's name cannot starts with a digit"
    const val PRODUCT_DESCRIPTION_STARTS_WITH_DIGIT = "The product's description cannot starts with a digit"

    const val COUNTRY_IS_EMPTY= "Country cannot be empty"
    const val COUNTRY_STARTS_WITH_LOWERCASE = "Country cannot starts with lowercase"
    const val COUNTRY_STARTS_WITH_DIGIT = "Country cannot starts with a digit"
    const val CITY_IS_EMPTY= "City cannot be empty"
    const val CITY_STARTS_WITH_LOWERCASE = "City cannot starts with lowercase"
    const val CITY_STARTS_WITH_DIGIT = "City cannot starts with a digit"
    const val STREET_IS_EMPTY= "Street cannot be empty"
    const val STREET_STARTS_WITH_LOWERCASE = "Street cannot starts with lowercase"
    const val STREET_STARTS_WITH_DIGIT = "Street cannot starts with a digit"
    const val WRONG_STREET_FORMAT = "Wrong format of the street"
    const val TYPE_IS_EMPTY= "Type cannot be empty"
    const val TYPE_STARTS_WITH_LOWERCASE = "City cannot starts with lowercase"
    const val TYPE_CONTAINS_DIGITS = "Type cannot contains digits"
    const val PHONE_NUMBER_IS_EMPTY= "Phone number cannot be empty"
    const val WRONG_PHONE_NUMBER_FORMAT = "Wrong format of the phone number"
}