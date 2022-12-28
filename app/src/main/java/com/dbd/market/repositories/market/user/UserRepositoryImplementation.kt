package com.dbd.market.repositories.market.user

import com.dbd.market.data.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject

class UserRepositoryImplementation @Inject constructor(private val userDocumentReference: DocumentReference?): UserRepository {

    override fun getUser(onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        userDocumentReference?.addSnapshotListener { value, error ->
            if (error != null) onFailure(error.message.toString())
            value?.let { userDocumentSnapshot ->
                val user = userDocumentSnapshot.toObject<User>()
                onSuccess(user!!)
            }
        }
    }
}