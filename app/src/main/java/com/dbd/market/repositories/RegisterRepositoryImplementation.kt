package com.dbd.market.repositories

import com.dbd.market.data.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import javax.inject.Inject

class RegisterRepositoryImplementation @Inject constructor(private val firebaseAuth: FirebaseAuth): RegisterRepository {

    override fun createUserWithEmailAndPassword(user: User, password: String, onSuccess: (AuthResult) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener {
                onSuccess(it)
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }
}