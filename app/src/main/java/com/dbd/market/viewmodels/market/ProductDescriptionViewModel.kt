package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.CartProduct
import com.dbd.market.helpers.operations.UserCartProductsFirestoreOperations
import com.dbd.market.repositories.market.product_description.ProductDescriptionRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _productDescriptionIncreaseAndDecreaseProgressBar = MutableStateFlow<Resource<Boolean>>(Resource.Undefined())
    val productDescriptionIncreaseAndDecreaseProgressBar = _productDescriptionIncreaseAndDecreaseProgressBar.asStateFlow()

    private val _productDescriptionIncreaseAndDecreaseVisibilityButtonsState = MutableStateFlow(false)
    val productDescriptionIncreaseAndDecreaseVisibilityButtonsState = _productDescriptionIncreaseAndDecreaseVisibilityButtonsState.asStateFlow()

    fun checkIfProductIsAlreadyInCart(cartProduct: CartProduct, cartProductId: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            productDescriptionRepository.checkIfProductIsAlreadyInCart(cartProduct, isAdded = { isAdded, takenCartProductId ->
                _productDescriptionIncreaseAndDecreaseVisibilityButtonsState.value = isAdded
                if (takenCartProductId.isNotEmpty()) cartProductId(takenCartProductId)
                else cartProductId("")
            },
            onFailure = { _productDescriptionProductAddedToCart.value = Resource.Error(it) })
        }
    }

    fun addProductToCart(cartProduct: CartProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            _productDescriptionProductAddedToCart.value = Resource.Loading()
            productDescriptionRepository.addProductToCart(cartProduct, onSuccess = {
                _productDescriptionProductAddedToCart.value = Resource.Success(true)
            },
            onFailure = { _productDescriptionProductAddedToCart.value = Resource.Error(it) })
        }
    }

    fun increaseCartProductQuantity(cartProductId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _productDescriptionIncreaseAndDecreaseProgressBar.value = Resource.Loading()
            userCartProductsFirestoreOperations.increaseCartProductQuantity(cartProductId).addOnCompleteListener {
                if (it.isSuccessful) _productDescriptionIncreaseAndDecreaseProgressBar.value = Resource.Success(true)
                else _productDescriptionIncreaseAndDecreaseProgressBar.value = Resource.Error(it.exception.toString())
            }
        }
    }

    fun decreaseCartProductQuantity(cartProductId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _productDescriptionIncreaseAndDecreaseProgressBar.value = Resource.Loading()
            userCartProductsFirestoreOperations.decreaseCartProductQuantity(cartProductId).addOnCompleteListener {
                if (it.isSuccessful) _productDescriptionIncreaseAndDecreaseProgressBar.value = Resource.Success(true)
                else _productDescriptionIncreaseAndDecreaseProgressBar.value = Resource.Error(it.exception.toString())
            }
        }
    }

    fun changeProductDescriptionSelectedSizeValue(size: String) { _productDescriptionSelectedSize.value = size }
}