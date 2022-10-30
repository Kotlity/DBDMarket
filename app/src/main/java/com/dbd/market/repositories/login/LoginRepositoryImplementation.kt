package com.dbd.market.repositories.login

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImplementation @Inject constructor(private val firebaseAuth: FirebaseAuth): LoginRepository {

    override fun loginUserWithEmailAndPassword(email: String, password: String, onSuccess: (AuthResult) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                onSuccess(authResult)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}