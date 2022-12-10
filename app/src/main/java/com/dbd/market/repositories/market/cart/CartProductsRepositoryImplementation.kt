package com.dbd.market.repositories.market.cart

import com.dbd.market.data.CartProduct
import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject

class CartProductsRepositoryImplementation @Inject constructor(@UserCartProductsCollectionReference private val userCartProductsCollectionReference: CollectionReference?): CartProductsRepository {

    override fun getCartProducts(cartProducts: (List<CartProduct>) -> Unit, onFailure: (String) -> Unit) {
        userCartProductsCollectionReference?.addSnapshotListener { value, error ->
            if (error != null) onFailure(error.message.toString())
            value?.let { querySnapshot ->
                val takenCartProducts = querySnapshot.toObjects<CartProduct>()
                cartProducts(takenCartProducts)
            }
        }
    }
}