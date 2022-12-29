package com.dbd.market.repositories.market.user

import android.net.Uri
import com.dbd.market.data.User
import com.dbd.market.utils.Constants.FIREBASE_STORAGE_USERS_IMAGES_PATH
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class UserRepositoryImplementation @Inject constructor(
    private val userDocumentReference: DocumentReference?,
    private val storageReference: StorageReference,
    private val userUid: String?
    ): UserRepository {

    override fun getUser(onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        userDocumentReference?.addSnapshotListener { value, error ->
            if (error != null) onFailure(error.message.toString())
            value?.let { userDocumentSnapshot ->
                val user = userDocumentSnapshot.toObject<User>()
                onSuccess(user!!)
            }
        }
    }

    override fun uploadUserImageToFirebaseStorage(imageUri: Uri, imageName: String, onSuccess: (Uri) -> Unit, onFailure: (String) -> Unit) {
        val userImagesStorageReference = storageReference.child(FIREBASE_STORAGE_USERS_IMAGES_PATH).child(userUid!!).child(imageName)
        userImagesStorageReference.putFile(imageUri).addOnSuccessListener {
            userImagesStorageReference.downloadUrl
                .addOnSuccessListener { uploadedUri -> onSuccess(uploadedUri) }
                .addOnFailureListener { downloadingImageUrlFromFirebaseStorageError -> onFailure(downloadingImageUrlFromFirebaseStorageError.message.toString()) }
        }.addOnFailureListener { addingImageToUserImagesStorageReferenceError -> onFailure(addingImageToUserImagesStorageReferenceError.message.toString()) }
    }

    override fun uploadUserImageToFirebaseFirestore(userImage: MutableMap<String, Any>, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userDocumentReference?.update(userImage)?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { updatingUserImageError -> onFailure(updatingUserImageError.message.toString()) }
    }
}