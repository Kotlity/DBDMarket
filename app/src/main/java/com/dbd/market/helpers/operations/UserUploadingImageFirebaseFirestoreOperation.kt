package com.dbd.market.helpers.operations

import com.google.firebase.firestore.DocumentReference
import javax.inject.Inject

class UserUploadingImageFirebaseFirestoreOperation @Inject constructor(private val userDocumentReference: DocumentReference?) {

    fun uploadUserImageToFirebaseFirestore(userImage: MutableMap<String, Any>, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userDocumentReference?.update(userImage)?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { updatingUserImageError -> onFailure(updatingUserImageError.message.toString()) }
    }
}