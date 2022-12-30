package com.dbd.market.repositories.market.addresses

import com.dbd.market.data.Address
import com.dbd.market.di.qualifiers.UserAddressesCollectionReference
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_USER_ADDRESSES_ID_FIELD
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject

class AddressesRepositoryImplementation @Inject constructor(@UserAddressesCollectionReference private val  userAddressesCollectionReference: CollectionReference?): AddressesRepository {

    override fun getAllAddresses(onSuccess: (List<Address>) -> Unit, onFailure: (String) -> Unit) {
        userAddressesCollectionReference?.orderBy(FIREBASE_FIRESTORE_USER_ADDRESSES_ID_FIELD, Query.Direction.DESCENDING)
            ?.get()
            ?.addOnSuccessListener { allAddressesQuerySnapshot ->
                val allAddresses = allAddressesQuerySnapshot.toObjects<Address>()
                onSuccess(allAddresses)
            }
            ?.addOnFailureListener { gettingAllAddressesError -> onFailure(gettingAllAddressesError.message.toString()) }
    }
}