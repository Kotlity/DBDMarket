package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        getCartProductsSize()
    }

    private fun getCartProductsSize() {
        viewModelScope.launch(Dispatchers.IO) {
            cartProductsRepository.getCartProductsSize(cartProductsSize = { _cartProductsSize.value = Resource.Success(it) },
            onFailure = { _cartProductsSize.value = Resource.Error(it.message.toString()) })
        }
    }
}