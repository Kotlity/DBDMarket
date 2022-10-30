package com.dbd.market.repositories.login

import com.google.firebase.auth.AuthResult
import java.lang.Exception

interface LoginRepository {

    fun loginUserWithEmailAndPassword(email: String, password: String, onSuccess: (AuthResult) -> Unit, onFailure: (Exception) -> Unit)
    fun resetPasswordWithEmail(email: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
}