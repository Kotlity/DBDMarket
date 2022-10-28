package com.dbd.market.repositories

import com.dbd.market.data.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import javax.inject.Inject

class RegisterRepositoryImplementation @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
    ): RegisterRepository {

    override fun createUserWithEmailAndPassword(user: User, password: String, onSuccess: (AuthResult) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { authResult ->
                onSuccess(authResult)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    override fun saveUserToFirebaseFirestore(userUID: String, user: User, collectionPath: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firebaseFirestore.collection(collectionPath).document(userUID).set(user).addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}