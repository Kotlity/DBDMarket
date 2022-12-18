package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.Address
import com.dbd.market.data.CartProduct
import com.dbd.market.helpers.operations.UserAddressesFirestoreOperations
import com.dbd.market.repositories.market.cart.CartProductsRepository
import com.dbd.market.repositories.market.setup_order.SetupOrderRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupOrderViewModel @Inject constructor(
    private val cartProductsRepository: CartProductsRepository,
    private val setupOrderRepository: SetupOrderRepository,
    private val userAddressesFirestoreOperations: UserAddressesFirestoreOperations
    ): ViewModel() {

    private val _setupOrderCartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Undefined())
    val setupOrderCartProducts = _setupOrderCartProducts.asStateFlow()

    private val _setupOrderAddresses = MutableStateFlow<Resource<List<Address>>>(Resource.Undefined())
    val setupOrderAddresses = _setupOrderAddresses.asStateFlow()

    private val _setupOrderAddAddress = MutableSharedFlow<Resource<Boolean>>()
    val setupOrderAddedAddress = _setupOrderAddAddress.asSharedFlow()

    private val _setupOrderDeleteAddress = MutableSharedFlow<Resource<Boolean>>()
    val setupOrderDeleteAddress = _setupOrderDeleteAddress.asSharedFlow()

    init {
        getSetupOrderCartProducts()
        getAddresses()
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

    private fun getAddresses() {
        viewModelScope.launch(Dispatchers.IO) {
            _setupOrderAddresses.value = Resource.Loading()
            setupOrderRepository.getAllAddresses(onSuccess = { listOfAddresses -> _setupOrderAddresses.value = Resource.Success(listOfAddresses)},
            onFailure = { gettingAddressesError -> _setupOrderAddresses.value = Resource.Error(gettingAddressesError) })
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            _setupOrderAddAddress.emit(Resource.Loading())
            setupOrderRepository.addAddress(address, onSuccess = { viewModelScope.launch(Dispatchers.IO) { _setupOrderAddAddress.emit(Resource.Success(true)) } },
            onFailure = { addingAddressError -> viewModelScope.launch(Dispatchers.IO) { _setupOrderAddAddress.emit(Resource.Error(addingAddressError)) } })
        }
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            _setupOrderDeleteAddress.emit(Resource.Loading())
            userAddressesFirestoreOperations.retrieveAddress(address, onSuccess = { addressesQuerySnapshot ->
                if (!addressesQuerySnapshot.isEmpty) {
                    val addAddressDocumentSnapshotId = addressesQuerySnapshot.documents[0].id
                    userAddressesFirestoreOperations.deleteAddress(addAddressDocumentSnapshotId)?.addOnSuccessListener { viewModelScope.launch(Dispatchers.IO) { _setupOrderDeleteAddress.emit(Resource.Success(true)) } }
                    ?.addOnFailureListener { deletingAddressError -> viewModelScope.launch(Dispatchers.IO) { _setupOrderDeleteAddress.emit(Resource.Error(deletingAddressError.message.toString())) } }
                }
            },
            onFailure = { retrievingAddressesError -> viewModelScope.launch(Dispatchers.IO) { _setupOrderDeleteAddress.emit(Resource.Error(retrievingAddressesError)) } })
        }
    }
}