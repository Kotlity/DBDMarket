package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.CartProduct
import com.dbd.market.helpers.operations.UserCartProductsFirestoreOperations
import com.dbd.market.repositories.market.cart.CartProductsRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartProductsRepository: CartProductsRepository,
    private val userCartProductsFirestoreOperations: UserCartProductsFirestoreOperations
    ): ViewModel() {

    private val _cartProductsSize = MutableStateFlow<Resource<Int>>(Resource.Loading())
    val cartProductsSize = _cartProductsSize.asStateFlow()

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Undefined())
    val cartProducts = _cartProducts.asStateFlow()

    private val _cartProductPlus = MutableStateFlow<Resource<Int>>(Resource.Undefined())
    val cartProductPlus = _cartProductPlus.asStateFlow()

    private val _cartProductMinus = MutableStateFlow<Resource<Int>>(Resource.Undefined())
    val cartProductMinus = _cartProductMinus.asStateFlow()

    private val _cartProductDelete = MutableStateFlow<Resource<Boolean>>(Resource.Undefined())
    val cartProductDelete = _cartProductDelete.asStateFlow()

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

    fun increaseCartProductQuantity(cartProduct: CartProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            _cartProductPlus.value = Resource.Loading()
            userCartProductsFirestoreOperations.retrieveCartProductInCart(cartProduct, onSuccess = { cartProductQuerySnapshot ->
                if (!cartProductQuerySnapshot.isEmpty) {
                    val takenCartProductDocumentSnapshot = cartProductQuerySnapshot.documents[0]
                    val takenCartProductDocumentSnapshotId = takenCartProductDocumentSnapshot.id
                    userCartProductsFirestoreOperations.increaseCartProductQuantity(takenCartProductDocumentSnapshotId).addOnCompleteListener {
                        if (it.isSuccessful) _cartProductPlus.value = Resource.Success(cartProduct.amount + 1)
                        else _cartProductPlus.value = Resource.Error(it.exception.toString())
                    }
                }
            },
            onFailure = { retrievingCartProductsException -> _cartProductPlus.value = Resource.Error(retrievingCartProductsException)})
        }
    }

    fun decreaseCartProductQuantity(cartProduct: CartProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            _cartProductMinus.value = Resource.Loading()
            userCartProductsFirestoreOperations.retrieveCartProductInCart(cartProduct, onSuccess = { cartProductQuerySnapshot ->
                if (!cartProductQuerySnapshot.isEmpty) {
                    val takenCartProductDocumentSnapshot = cartProductQuerySnapshot.documents[0]
                    val takenCartProductDocumentSnapshotId= takenCartProductDocumentSnapshot.id
                    userCartProductsFirestoreOperations.decreaseCartProductQuantity(takenCartProductDocumentSnapshotId).addOnCompleteListener {
                        if (it.isSuccessful) _cartProductMinus.value = Resource.Success(cartProduct.amount - 1)
                        else _cartProductMinus.value = Resource.Error(it.exception.toString())
                    }
                }
            },
            onFailure = { retrievingCartProductException -> _cartProductMinus.value = Resource.Error(retrievingCartProductException)})
        }
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            _cartProductDelete.value = Resource.Loading()
            userCartProductsFirestoreOperations.retrieveCartProductInCart(cartProduct, onSuccess = { cartProductQuerySnapshot ->
                if (!cartProductQuerySnapshot.isEmpty) {
                    val takenCartProductDocumentSnapshotId = cartProductQuerySnapshot.documents[0].id
                    userCartProductsFirestoreOperations.deleteCartProduct(takenCartProductDocumentSnapshotId)?.addOnCompleteListener {
                        if (it.isSuccessful) _cartProductDelete.value = Resource.Success(true)
                        else _cartProductDelete.value = Resource.Error(it.exception.toString())
                    }
                }
            },
            onFailure = { retrievingCartProductsException -> _cartProductMinus.value = Resource.Error(retrievingCartProductsException)})
        }
    }
}