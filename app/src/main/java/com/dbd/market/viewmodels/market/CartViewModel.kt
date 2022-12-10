package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.CartProduct
import com.dbd.market.repositories.market.cart.CartProductsRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartProductsRepository: CartProductsRepository): ViewModel() {

    private val _cartProductsSize = MutableStateFlow<Resource<Int>>(Resource.Loading())
    val cartProductsSize = _cartProductsSize.asStateFlow()

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Undefined())
    val cartProducts = _cartProducts.asStateFlow()

    init {
        getCartProducts()
    }

    private fun getCartProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _cartProducts.value = Resource.Loading()
            cartProductsRepository.getCartProducts(cartProducts = { takenCartProducts ->
                val takenCartProductsSize = takenCartProducts.size
                _cartProductsSize.value = Resource.Success(takenCartProductsSize)
                _cartProducts.value = Resource.Success(takenCartProducts)
            },
            onFailure = { gettingCartProductsError ->
                _cartProductsSize.value = Resource.Error(gettingCartProductsError)
                _cartProducts.value = Resource.Error(gettingCartProductsError)
            })
        }
    }
}