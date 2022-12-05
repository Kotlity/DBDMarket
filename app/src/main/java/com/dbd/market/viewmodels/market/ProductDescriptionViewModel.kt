package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import com.dbd.market.data.CartProduct
import com.dbd.market.helpers.operations.UserCartProductsFirestoreOperations
import com.dbd.market.repositories.market.product_description.ProductDescriptionRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDescriptionViewModel @Inject constructor(
    private val productDescriptionRepository: ProductDescriptionRepository,
    private val userCartProductsFirestoreOperations: UserCartProductsFirestoreOperations
    ): ViewModel() {

    private val _productDescriptionSelectedSize = MutableStateFlow("")
    val productDescriptionSelectedSize = _productDescriptionSelectedSize.asStateFlow()

    private val _productDescriptionProductAddedToCart = MutableStateFlow<Resource<Boolean>>(Resource.Undefined())
    val productDescriptionProductAddedToCart = _productDescriptionProductAddedToCart.asStateFlow()

    fun addProductToCart(cartProduct: CartProduct) {
        _productDescriptionProductAddedToCart.value = Resource.Loading()
            productDescriptionRepository.checkIfProductIsAlreadyInCart(cartProduct, isAdded = { isAdded, takenCartProductId ->
                if (isAdded) {
                    userCartProductsFirestoreOperations.increaseCartProductQuantity(takenCartProductId).addOnCompleteListener {
                        if (it.isSuccessful) _productDescriptionProductAddedToCart.value = Resource.Success(true)
                        else _productDescriptionProductAddedToCart.value = Resource.Error(it.exception.toString())
                    }
                } else {
                    productDescriptionRepository.addProductToCart(cartProduct, onSuccess = { _productDescriptionProductAddedToCart.value = Resource.Success(true) },
                        onFailure = { addingProductToCartException -> _productDescriptionProductAddedToCart.value = Resource.Error(addingProductToCartException) })
                }
            },
            onFailure = {
                _productDescriptionProductAddedToCart.value = Resource.Error(it)
            })
    }

    fun changeProductDescriptionSelectedSizeValue(size: String) { _productDescriptionSelectedSize.value = size }
}