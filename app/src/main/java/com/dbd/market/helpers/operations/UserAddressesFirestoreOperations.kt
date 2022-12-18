package com.dbd.market.helpers.operations

import com.dbd.market.data.Address
import com.dbd.market.di.qualifiers.UserAddressesCollectionReference
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_ADDRESSES_ID_FIELD
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class UserAddressesFirestoreOperations @Inject constructor(@UserAddressesCollectionReference private val userAddressesCollectionReference: CollectionReference?) {

    fun retrieveAddress(address: Address, onSuccess: (QuerySnapshot) -> Unit, onFailure: (String) -> Unit) {
        userAddressesCollectionReference?.whereEqualTo(FIREBASE_FIRESTORE_ADDRESSES_ID_FIELD, address.id)?.get()
            ?.addOnSuccessListener { retrievingAddressQuerySnapshot -> onSuccess(retrievingAddressQuerySnapshot) }
            ?.addOnFailureListener { retrievingAddressException -> onFailure(retrievingAddressException.message.toString()) }
    }

    fun deleteAddress(addressDocumentSnapshotId: String) = userAddressesCollectionReference?.document(addressDocumentSnapshotId)?.delete()
}