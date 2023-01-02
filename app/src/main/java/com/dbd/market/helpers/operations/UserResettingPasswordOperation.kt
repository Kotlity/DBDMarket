package com.dbd.market.helpers.operations

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserResettingPasswordOperation @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun resetUserPasswordViaEmail(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { resettingUserPasswordError -> onFailure(resettingUserPasswordError.message.toString()) }
    }
}