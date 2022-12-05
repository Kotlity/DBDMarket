package com.dbd.market.repositories.market.product_description

import com.dbd.market.data.CartProduct
import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.dbd.market.helpers.operations.UserCartProductsFirestoreOperations
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class ProductDescriptionRepositoryImplementation @Inject constructor(
    @UserCartProductsCollectionReference private val userCartProductsCollectionReference: CollectionReference?,
    private val userCartProductsFirestoreOperations: UserCartProductsFirestoreOperations): ProductDescriptionRepository {

    override fun addProductToCart(cartProduct: CartProduct, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        userCartProductsCollectionReference?.document()?.set(cartProduct)
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { addingProductToCartException -> onFailure(addingProductToCartException.message.toString()) }
    }

    override fun checkIfProductIsAlreadyInCart(cartProduct: CartProduct, isAdded: (Boolean, String) -> Unit, onFailure: (String) -> Unit) {
        userCartProductsFirestoreOperations.retrieveCartProductInCart(cartProduct, onSuccess = { cartProductInCartQuerySnapshot ->
            if (!cartProductInCartQuerySnapshot.isEmpty) {
                val cartProductDocumentId = cartProductInCartQuerySnapshot.documents[0].id
                isAdded(true, cartProductDocumentId)
            } else isAdded(false, "")
        },
        onFailure = { retrievingCartProductInCartException -> onFailure(retrievingCartProductInCartException) })
    }
}