package com.dbd.market.repositories.market.cart

import com.google.firebase.firestore.FirebaseFirestoreException

interface CartProductsRepository {

    fun getCartProductsSize(cartProductsSize: (Int) -> Unit, onFailure: (FirebaseFirestoreException) -> Unit)
}