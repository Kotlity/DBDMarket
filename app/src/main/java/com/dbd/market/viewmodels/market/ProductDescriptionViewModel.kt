package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.CartProduct
import com.dbd.market.repositories.market.product_description.ProductDescriptionRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDescriptionViewModel @Inject constructor(private val productDescriptionRepository: ProductDescriptionRepository): ViewModel() {

    private val _productDescriptionSelectedSize = MutableStateFlow("")
    val productDescriptionSelectedSize = _productDescriptionSelectedSize.asStateFlow()

    private val _productDescriptionProductAddedToCart = MutableStateFlow<Resource<Boolean>>(Resource.Undefined())
    val productDescriptionProductAddedToCart = _productDescriptionProductAddedToCart.asStateFlow()

    fun addProductToCart(cartProduct: CartProduct) {
        _productDescriptionProductAddedToCart.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            productDescriptionRepository.addProductToCart(cartProduct, onSuccess = { _productDescriptionProductAddedToCart.value = Resource.Success(true) },
            onFailure = { addingProductToCartException -> _productDescriptionProductAddedToCart.value = Resource.Error(addingProductToCartException) })
        }
    }

    fun changeProductDescriptionSelectedSizeValue(size: String) { _productDescriptionSelectedSize.value = size }
}