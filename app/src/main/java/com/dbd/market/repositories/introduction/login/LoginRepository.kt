package com.dbd.market.repositories.introduction.login

import com.dbd.market.data.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import java.lang.Exception

interface LoginRepository {

    fun loginUserWithEmailAndPassword(email: String, password: String, onSuccess: (AuthResult) -> Unit, onFailure: (Exception) -> Unit)

    fun signInWithGoogle(credential: AuthCredential): Task<AuthResult>

    fun saveUserInformationToFirebaseFirestore(userUid: String, user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit)
}