package com.dbd.market.repositories.introduction.login

import com.dbd.market.data.User
import com.dbd.market.di.qualifiers.UsersCollectionReference
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImplementation @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @UsersCollectionReference private val usersCollectionReference: CollectionReference): LoginRepository {

    override fun loginUserWithEmailAndPassword(email: String, password: String, onSuccess: (AuthResult) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                onSuccess(authResult)
            }
            .addOnFailureListener { loginException ->
                onFailure(loginException)
            }
    }

    override fun signInWithGoogle(credential: AuthCredential) = firebaseAuth.signInWithCredential(credential)

    override fun saveUserInformationToFirebaseFirestore(userUid: String, user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        usersCollectionReference.document(userUid).set(user)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                    savingUserInformationToFirebaseFirestoreError -> onFailure(savingUserInformationToFirebaseFirestoreError.message.toString())
            }
    }
}