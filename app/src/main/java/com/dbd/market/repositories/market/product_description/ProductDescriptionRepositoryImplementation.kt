package com.dbd.market.repositories.market.product_description

import com.dbd.market.data.CartProduct
import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDescriptionRepositoryImplementation @Inject constructor(@UserCartProductsCollectionReference private val userCartProductsCollectionReference: CollectionReference?): ProductDescriptionRepository {

    override suspend fun addProductToCart(cartProduct: CartProduct, result: (Boolean, String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userCartProductsCollectionReference?.document()?.set(cartProduct)
                result(true, "")
            } catch (e: Exception) { result(false, e.message.toString()) }
        }
    }
}