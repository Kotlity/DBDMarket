package com.dbd.market.repositories.market.product_description

import com.dbd.market.data.CartProduct

interface ProductDescriptionRepository {

    fun addProductToCart(cartProduct: CartProduct, onSuccess: () -> Unit, onFailure: (String) -> Unit)

    fun checkIfProductIsAlreadyInCart(cartProduct: CartProduct, isAdded: (Boolean, String) -> Unit, onFailure: (String) -> Unit)
}