package com.dbd.market.helpers.operations

import com.dbd.market.data.CartProduct
import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_CART_PRODUCTS_AMOUNT_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_CART_PRODUCTS_ID_FIELD
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_CART_PRODUCTS_SIZE_FIELD
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import javax.inject.Inject

class UserCartProductsFirestoreOperations @Inject constructor(
    @UserCartProductsCollectionReference private val userCartProductsCollectionReference: CollectionReference?,
    private val firebaseFirestore: FirebaseFirestore) {

    fun retrieveCartProductInCart(cartProduct: CartProduct, onSuccess: (QuerySnapshot) -> Unit, onFailure: (String) -> Unit) {
        userCartProductsCollectionReference
            ?.whereEqualTo(FIREBASE_FIRESTORE_CART_PRODUCTS_ID_FIELD, cartProduct.id)
            ?.whereEqualTo(FIREBASE_FIRESTORE_CART_PRODUCTS_SIZE_FIELD, cartProduct.size)?.get()
            ?.addOnSuccessListener { cartProductInCartQuerySnapshot -> onSuccess(cartProductInCartQuerySnapshot)}
            ?.addOnFailureListener { retrievingCartProductInCartException -> onFailure(retrievingCartProductInCartException.message.toString())}
    }


    fun increaseCartProductQuantity(cartProductId: String): Task<Transaction> {
        val cartProductDocumentReference =
            userCartProductsCollectionReference?.document(cartProductId)
        return firebaseFirestore.runTransaction { transaction ->
            val cartProductSnapshot = transaction.get(cartProductDocumentReference!!)
            val newCartProductQuantity = cartProductSnapshot.getLong(FIREBASE_FIRESTORE_CART_PRODUCTS_AMOUNT_FIELD)!! + 1
            transaction.update(cartProductDocumentReference, FIREBASE_FIRESTORE_CART_PRODUCTS_AMOUNT_FIELD, newCartProductQuantity)
        }
    }
}