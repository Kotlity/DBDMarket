package com.dbd.market.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.dbd.market.R
import com.dbd.market.data.Address
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.random.Random

fun Fragment.showDialogForResettingPassword(
    context: Context,
    title: String,
    description: String,
    titleOfNegativeButton: String,
    titleOfPositiveButton: String,
    onPositiveButtonClick: (String) -> Unit
) {
    val dialog = Dialog(context, R.style.Theme_Dialog)
    dialog.window?.apply {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestFeature(Window.FEATURE_NO_TITLE)
    }
    val view = layoutInflater.inflate(R.layout.reset_password_dialog_layout, null)

    view.findViewById<TextView>(R.id.resetPasswordTitleTextView).text = title
    view.findViewById<TextView>(R.id.resetPasswordDescriptionTextView).text = description
    val emailEditText = view.findViewById<EditText>(R.id.dialogResetPasswordViaEmailEditText)
    val negativeButton = view.findViewById<AppCompatButton>(R.id.appCancelResetPasswordButton)
    val positiveButton = view.findViewById<AppCompatButton>(R.id.appSendResetPasswordButton)

    negativeButton.text = titleOfNegativeButton
    positiveButton.text = titleOfPositiveButton

    negativeButton.setOnClickListener {
        dialog.dismiss()
    }
    positiveButton.setOnClickListener {
        val inputEmailAddress = emailEditText.text.toString().trim()
        if (checkValidationEmail(inputEmailAddress) is ValidationStatus.Success) {
            onPositiveButtonClick(inputEmailAddress)
            dialog.dismiss()
        } else {
            val resetPasswordFieldState = ResetPasswordFieldState(checkValidationEmail(inputEmailAddress))
            if (resetPasswordFieldState.email is ValidationStatus.Error) {
                positiveButton.apply {
                    requestFocus()
                    error = resetPasswordFieldState.email.errorMessage
                }
            }
        }
    }

    dialog.apply {
        setCancelable(false)
        setContentView(view)
        show()
    }
}

fun showCustomAlertDialog(
    context: Context,
    title: String,
    message: String,
    onPositiveButtonClick:() -> Unit
) {
    val alertDialog = AlertDialog.Builder(context).create()
    val alertDialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog_layout, null)
    alertDialog.apply {
        setView(alertDialogView)
        setCancelable(false)
    }
    alertDialogView.findViewById<TextView>(R.id.customAlertDialogTitleTextView).text = title
    alertDialogView.findViewById<TextView>(R.id.customAlertDialogMessageTextView).text = message
    alertDialogView.findViewById<AppCompatButton>(R.id.customAlertDialogCancelButton).setOnClickListener { alertDialog.dismiss() }
    alertDialogView.findViewById<AppCompatButton>(R.id.customAlertDialogApplyButton).setOnClickListener {
        onPositiveButtonClick()
        alertDialog.dismiss()
    }
    alertDialog.show()
}

fun showBottomSheetDialog(
    context: Context,
    onSuccess: (Address) -> Unit
) {
    val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogStyle)
    val bottomSheetDialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_address_dialog, null)
    bottomSheetDialog.setContentView(bottomSheetDialogView)
    bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    bottomSheetDialog.setCancelable(false)
    bottomSheetDialog.show()

    val firstNameAddressEditText = bottomSheetDialogView.findViewById<EditText>(R.id.firstNameAddressEditText)
    val lastNameAddressEditText = bottomSheetDialogView.findViewById<EditText>(R.id.lastNameAddressEditText)
    val countryAddressEditText = bottomSheetDialogView.findViewById<EditText>(R.id.countryAddressEditText)
    val cityAddressEditText = bottomSheetDialogView.findViewById<EditText>(R.id.cityAddressEditText)
    val streetAddressEditText = bottomSheetDialogView.findViewById<EditText>(R.id.streetAddressEditText)
    val typeAddressEditText = bottomSheetDialogView.findViewById<EditText>(R.id.typeAddressEditText)
    val phoneNumberAddressEditText = bottomSheetDialogView.findViewById<EditText>(R.id.phoneNumberAddressEditText)

    fun isCorrectedAddressesEditTextsInput(): Boolean {
        val firstNameAddress = firstNameAddressEditText.text.toString()
        val lastNameAddress = lastNameAddressEditText.text.toString()
        val countryAddress = countryAddressEditText.text.toString()
        val cityAddress = cityAddressEditText.text.toString()
        val streetAddress = streetAddressEditText.text.toString()
        val typeAddress = typeAddressEditText.text.toString()
        val phoneNumberAddress = phoneNumberAddressEditText.text.toString()

        val validationFirstNameAddress = checkValidationFirstname(firstNameAddress)
        val validationLastNameAddress = checkValidationLastname(lastNameAddress)
        val validationCountryAddress = checkValidationCountry(countryAddress)
        val validationCityAddress = checkValidationCity(cityAddress)
        val validationStreetAddress = checkValidationStreet(streetAddress)
        val validationTypeAddress = checkValidationType(typeAddress)
        val validationPhoneNumberAddress = checkValidationPhoneNumber(phoneNumberAddress)

        return (validationFirstNameAddress is ValidationStatus.Success
                && validationLastNameAddress is ValidationStatus.Success
                && validationCountryAddress is ValidationStatus.Success
                && validationCityAddress is ValidationStatus.Success
                && validationStreetAddress is ValidationStatus.Success
                && validationTypeAddress is ValidationStatus.Success
                && validationPhoneNumberAddress is ValidationStatus.Success
                )
    }

    fun showErrorMessagesInAddressesEditTexts() {
        val firstNameAddress = firstNameAddressEditText.text.toString()
        val lastNameAddress = lastNameAddressEditText.text.toString()
        val countryAddress = countryAddressEditText.text.toString()
        val cityAddress = cityAddressEditText.text.toString()
        val streetAddress = streetAddressEditText.text.toString()
        val typeAddress = typeAddressEditText.text.toString()
        val phoneNumberAddress = phoneNumberAddressEditText.text.toString()

        val validationFirstNameAddress = checkValidationFirstname(firstNameAddress)
        val validationLastNameAddress = checkValidationLastname(lastNameAddress)
        val validationCountryAddress = checkValidationCountry(countryAddress)
        val validationCityAddress = checkValidationCity(cityAddress)
        val validationStreetAddress = checkValidationStreet(streetAddress)
        val validationTypeAddress = checkValidationType(typeAddress)
        val validationPhoneNumberAddress = checkValidationPhoneNumber(phoneNumberAddress)

        val addAddressFieldsState = AddAddressFieldsState(
            validationFirstNameAddress,
            validationLastNameAddress,
            validationCountryAddress,
            validationCityAddress,
            validationStreetAddress,
            validationTypeAddress,
            validationPhoneNumberAddress
        )

        if (addAddressFieldsState.firstName is ValidationStatus.Error) {
            firstNameAddressEditText.apply {
                requestFocus()
                error = addAddressFieldsState.firstName.errorMessage
            }
        }
        if (addAddressFieldsState.lastName is ValidationStatus.Error) {
            lastNameAddressEditText.apply {
                requestFocus()
                error = addAddressFieldsState.lastName.errorMessage
            }
        }
        if (addAddressFieldsState.country is ValidationStatus.Error) {
            countryAddressEditText.apply {
                requestFocus()
                error = addAddressFieldsState.country.errorMessage
            }
        }
        if (addAddressFieldsState.city is ValidationStatus.Error) {
            cityAddressEditText.apply {
                requestFocus()
                error = addAddressFieldsState.city.errorMessage
            }
        }
        if (addAddressFieldsState.street is ValidationStatus.Error) {
            streetAddressEditText.apply {
                requestFocus()
                error = addAddressFieldsState.street.errorMessage
            }
        }
        if (addAddressFieldsState.type is ValidationStatus.Error) {
            typeAddressEditText.apply {
                requestFocus()
                error = addAddressFieldsState.type.errorMessage
            }
        }
        if (addAddressFieldsState.phoneNumber is ValidationStatus.Error) {
            phoneNumberAddressEditText.apply {
                requestFocus()
                error = addAddressFieldsState.phoneNumber.errorMessage
            }
        }
    }

    bottomSheetDialogView.findViewById<AppCompatButton>(R.id.addAddressButton).setOnClickListener {
        if (isCorrectedAddressesEditTextsInput()) {
            val firstNameAddress = firstNameAddressEditText.text.toString()
            val lastNameAddress = lastNameAddressEditText.text.toString()
            val countryAddress = countryAddressEditText.text.toString()
            val cityAddress = cityAddressEditText.text.toString()
            val streetAddress = streetAddressEditText.text.toString()
            val typeAddress = typeAddressEditText.text.toString()
            val phoneNumberAddress = phoneNumberAddressEditText.text.toString()

            val newAddress = Address(
                Random.nextInt(Constants.BOUND_OF_ADDRESS_ID),
                firstNameAddress,
                lastNameAddress,
                countryAddress,
                cityAddress,
                streetAddress,
                typeAddress,
                phoneNumberAddress)

            onSuccess(newAddress)
            bottomSheetDialog.dismiss()
        } else showErrorMessagesInAddressesEditTexts()
    }

    bottomSheetDialogView.findViewById<AppCompatButton>(R.id.closeBottomSheetDialogButton).setOnClickListener { bottomSheetDialog.dismiss() }
}