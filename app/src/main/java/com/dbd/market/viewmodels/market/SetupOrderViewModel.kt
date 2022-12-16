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
class SetupOrderViewModel @Inject constructor(private val cartProductsRepository: CartProductsRepository): ViewModel() {

    private val _setupOrderCartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Undefined())
    val setupOrderCartProducts = _setupOrderCartProducts.asStateFlow()

    init {
        getSetupOrderCartProducts()
    }

    private fun getSetupOrderCartProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _setupOrderCartProducts.value = Resource.Loading()
            cartProductsRepository.getCartProducts(cartProducts = { setupOrderCartProductsList ->
                _setupOrderCartProducts.value = Resource.Success(setupOrderCartProductsList)
            },
            onFailure = { gettingSetupOrderCartProductsError -> _setupOrderCartProducts.value = Resource.Error(gettingSetupOrderCartProductsError)})
        }
    }
}