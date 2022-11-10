package com.dbd.market.repositories.introduction.register

import com.dbd.market.data.User
import com.google.firebase.auth.AuthResult
import java.lang.Exception

interface RegisterRepository {

    fun createUserWithEmailAndPassword(user: User, password: String, onSuccess: (AuthResult) -> Unit, onFailure: (Exception) -> Unit)
    fun saveUserToFirebaseFirestore(userUID: String, user: User, collectionPath: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
}