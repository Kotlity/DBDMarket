package com.dbd.market.repositories.market.product_description

import com.dbd.market.data.CartProduct

interface ProductDescriptionRepository {

    suspend fun addProductToCart(cartProduct: CartProduct, result: (Boolean, String?) -> Unit)
}