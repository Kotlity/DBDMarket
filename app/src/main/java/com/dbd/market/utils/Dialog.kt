package com.dbd.market.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.dbd.market.R

fun Fragment.showDialog(
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