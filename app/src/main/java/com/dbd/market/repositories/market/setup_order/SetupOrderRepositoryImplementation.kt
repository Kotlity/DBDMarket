package com.dbd.market.repositories.market.setup_order

import com.dbd.market.data.Address
import com.dbd.market.data.Order
import com.dbd.market.di.qualifiers.UserAddressesCollectionReference
import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.dbd.market.di.qualifiers.UserOrderCollectionReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject

class SetupOrderRepositoryImplementation @Inject constructor(
    @UserAddressesCollectionReference private val userAddressesCollectionReference: CollectionReference?,
    @UserCartProductsCollectionReference private val userCartProductsCollectionReference: CollectionReference?,
    @UserOrderCollectionReference private val userOrderCollectionReference: CollectionReference?,
    private val firebaseFirestore: FirebaseFirestore): SetupOrderRepository {

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

    override fun deleteAllCartProductsFromCollection(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userCartProductsCollectionReference?.get()?.addOnSuccessListener { cartProductsQuerySnapshot ->
            firebaseFirestore.runBatch { batch ->
                val cartProductsDocumentsSnapshot = cartProductsQuerySnapshot.documents
                cartProductsDocumentsSnapshot.forEach { cartProductDocumentSnapshot ->
                    val cartProductDocumentReference = cartProductDocumentSnapshot.reference
                    batch.delete(cartProductDocumentReference)
                }
            }
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { deletingCartProductsError -> onFailure(deletingCartProductsError.message.toString()) }
        }
        ?.addOnFailureListener { gettingCartProductsError -> onFailure(gettingCartProductsError.message.toString()) }
    }

    override fun addSetupOrderToOrderCollection(order: Order, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userOrderCollectionReference?.document()?.set(order)
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { addingSetupOrderToOrderCollectionError -> onFailure(addingSetupOrderToOrderCollectionError.message.toString())}
    }
}