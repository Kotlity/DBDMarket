package com.dbd.market.repositories.market.cart

import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import javax.inject.Inject

class CartProductsRepositoryImplementation @Inject constructor(@UserCartProductsCollectionReference private val userCartProductsCollectionReference: CollectionReference?): CartProductsRepository {

    override fun getCartProductsSize(cartProductsSize: (Int) -> Unit, onFailure: (FirebaseFirestoreException) -> Unit) {
        userCartProductsCollectionReference?.addSnapshotListener { value, error ->
            if (error != null) onFailure(error)
            value?.let {
                val querySnapshotSize = it.size()
                cartProductsSize(querySnapshotSize)
            }
        }
    }
}