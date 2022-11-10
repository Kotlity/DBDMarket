package com.dbd.market.helpers.products_adder.validation

import androidx.core.text.isDigitsOnly
import com.dbd.market.utils.Constants.PRODUCT_CATEGORY_CONTAINS_DIGITS
import com.dbd.market.utils.Constants.PRODUCT_CATEGORY_IS_EMPTY
import com.dbd.market.utils.Constants.PRODUCT_DESCRIPTION_IS_EMPTY
import com.dbd.market.utils.Constants.PRODUCT_DESCRIPTION_MAX_LENGTH
import com.dbd.market.utils.Constants.PRODUCT_DESCRIPTION_STARTS_WITH_DIGIT
import com.dbd.market.utils.Constants.PRODUCT_DESCRIPTION_STARTS_WITH_LOWERCASE
import com.dbd.market.utils.Constants.PRODUCT_DESCRIPTION_WRONG_LENGTH
import com.dbd.market.utils.Constants.PRODUCT_IMAGE_IS_EMPTY
import com.dbd.market.utils.Constants.PRODUCT_NAME_IS_EMPTY
import com.dbd.market.utils.Constants.PRODUCT_NAME_MAX_LENGTH
import com.dbd.market.utils.Constants.PRODUCT_NAME_STARTS_WITH_DIGIT
import com.dbd.market.utils.Constants.PRODUCT_NAME_STARTS_WITH_LOWERCASE
import com.dbd.market.utils.Constants.PRODUCT_NAME_WRONG_LENGTH
import com.dbd.market.utils.Constants.PRODUCT_PRICE_CONTAINS_WRONG_INPUT
import com.dbd.market.utils.Constants.PRODUCT_PRICE_IS_EMPTY
import com.dbd.market.utils.Constants.WRONG_PRODUCT_CATEGORY_FORMAT
import com.dbd.market.utils.Constants.PRODUCT_PRICE_CONTAINS_SPACE
import com.dbd.market.utils.Constants.PRODUCT_PRICE_MAX_LENGTH
import com.dbd.market.utils.Constants.PRODUCT_PRICE_WRONG_LENGTH
import com.dbd.market.utils.Constants.PRODUCT_SIZE_CONTAINS_DIGITS
import com.dbd.market.utils.Constants.PRODUCT_SIZE_CONTAINS_LOWERCASE
import com.dbd.market.utils.Constants.PRODUCT_SIZE_IS_EMPTY
import com.dbd.market.utils.Constants.PRODUCT_SIZE_MAX_LENGTH
import com.dbd.market.utils.Constants.PRODUCT_SIZE_WRONG_LENGTH
import com.dbd.market.utils.ValidationStatus

fun checkNameEditTextInputValidation(input: String): ValidationStatus {
    return if (input.trim().isEmpty()) ValidationStatus.Error(PRODUCT_NAME_IS_EMPTY)
    else if (input.length > PRODUCT_NAME_MAX_LENGTH) ValidationStatus.Error(PRODUCT_NAME_WRONG_LENGTH)
    else if (input.trim().first().isDigit()) ValidationStatus.Error(PRODUCT_NAME_STARTS_WITH_DIGIT)
    else if (input.trim().first().isLowerCase()) ValidationStatus.Error(PRODUCT_NAME_STARTS_WITH_LOWERCASE)
    else ValidationStatus.Success
}

fun checkCategoryEditTextInputValidation(input: String): ValidationStatus {
    return if (input.trim().isEmpty()) ValidationStatus.Error(PRODUCT_CATEGORY_IS_EMPTY)
    else if (input.trim().contains("[0-9]".toRegex())) ValidationStatus.Error(PRODUCT_CATEGORY_CONTAINS_DIGITS)
    else if (!((input == "Special products") or (input == "Beneficial products") or (input == "Interesting products") or (input == "Suits") or (input == "Headdress") or (input == "Torso") or (input == "Weapon") or (input == "Legs"))) ValidationStatus.Error(WRONG_PRODUCT_CATEGORY_FORMAT)
    else ValidationStatus.Success
}

fun checkDescriptionEditTextInputValidation(input: String): ValidationStatus {
    return if (input.trim().isEmpty()) ValidationStatus.Error(PRODUCT_DESCRIPTION_IS_EMPTY)
    else if (input.length > PRODUCT_DESCRIPTION_MAX_LENGTH) ValidationStatus.Error(PRODUCT_DESCRIPTION_WRONG_LENGTH)
    else if (input.trim().first().isDigit()) ValidationStatus.Error(PRODUCT_DESCRIPTION_STARTS_WITH_DIGIT)
    else if (input.trim().first().isLowerCase()) ValidationStatus.Error(PRODUCT_DESCRIPTION_STARTS_WITH_LOWERCASE)
    else ValidationStatus.Success
}

fun checkPriceEditTextInputValidation(input: String): ValidationStatus {
    return if (input.trim().isEmpty()) ValidationStatus.Error(PRODUCT_PRICE_IS_EMPTY)
    else if (input.length > PRODUCT_PRICE_MAX_LENGTH) ValidationStatus.Error(PRODUCT_PRICE_WRONG_LENGTH)
    else if (input.contains(" ")) ValidationStatus.Error(PRODUCT_PRICE_CONTAINS_SPACE)
    else if (!input.isDigitsOnly()) ValidationStatus.Error(PRODUCT_PRICE_CONTAINS_WRONG_INPUT)
    else ValidationStatus.Success
}

fun checkSizeEditTextInputValidation(input: List<String>): ValidationStatus {
    input.forEach { size ->
        if (size.contains("[0-9]".toRegex())) return ValidationStatus.Error(PRODUCT_SIZE_CONTAINS_DIGITS)
    }
    input.forEach { size ->
        if (size.contains("[a-z]".toRegex())) return ValidationStatus.Error(PRODUCT_SIZE_CONTAINS_LOWERCASE)
    }
    input.forEach { size ->
        if (size.length > PRODUCT_SIZE_MAX_LENGTH) return ValidationStatus.Error(PRODUCT_SIZE_WRONG_LENGTH)
    }
    return if (input.isEmpty()) ValidationStatus.Error(PRODUCT_SIZE_IS_EMPTY)
    else ValidationStatus.Success
}

fun checkSelectedImageValidation(imagesList: List<String>): ValidationStatus {
    return if (imagesList.isEmpty()) ValidationStatus.Error(PRODUCT_IMAGE_IS_EMPTY)
    else ValidationStatus.Success
}