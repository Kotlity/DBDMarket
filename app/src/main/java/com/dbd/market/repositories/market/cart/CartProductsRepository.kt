package com.dbd.market.repositories.market.cart

import com.dbd.market.data.CartProduct

interface CartProductsRepository {

    fun getCartProducts(cartProducts: (List<CartProduct>) -> Unit, onFailure: (String) -> Unit)
}