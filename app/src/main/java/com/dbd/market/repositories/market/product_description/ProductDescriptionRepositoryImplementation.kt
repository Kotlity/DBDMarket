package com.dbd.market.repositories.market.product_description

import com.dbd.market.data.CartProduct
import com.dbd.market.di.qualifiers.UserCartProductsCollectionReference
import com.dbd.market.utils.Constants.ADD_TO_CART_DURATION_PERIOD
import com.dbd.market.utils.Constants.PLEASE_CHECK_INTERNET_CONNECTION
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class ProductDescriptionRepositoryImplementation @Inject constructor(@UserCartProductsCollectionReference private val userCartProductsCollectionReference: CollectionReference?): ProductDescriptionRepository {

    override suspend fun addProductToCart(cartProduct: CartProduct, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeout(ADD_TO_CART_DURATION_PERIOD) {
                    userCartProductsCollectionReference?.document()?.set(cartProduct)?.await()
                    onSuccess()
                }
            } catch (e: Exception) { onFailure(PLEASE_CHECK_INTERNET_CONNECTION) }
        }
    }
}