package com.dbd.market.repositories.market.setup_order

import com.dbd.market.data.Address
import com.dbd.market.di.qualifiers.UserAddressesCollectionReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject

class SetupOrderRepositoryImplementation @Inject constructor(@UserAddressesCollectionReference private val userAddressesCollectionReference: CollectionReference?): SetupOrderRepository {

    override fun getAllAddresses(onSuccess: (List<Address>) -> Unit, onFailure: (String) -> Unit) {
        userAddressesCollectionReference?.addSnapshotListener { value, error ->
            if (error != null) onFailure(error.message.toString())
            value?.let { userAddressesQuerySnapshot ->
                val listOfAddresses = userAddressesQuerySnapshot.toObjects<Address>()
                onSuccess(listOfAddresses)
            }
        }
    }

    override fun addAddress(address: Address, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userAddressesCollectionReference?.document()?.set(address)
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { addingAddressException -> onFailure(addingAddressException.message.toString())}
    }
}