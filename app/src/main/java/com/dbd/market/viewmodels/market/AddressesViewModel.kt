package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.Address
import com.dbd.market.repositories.market.addresses.AddressesRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(private val addressesRepository: AddressesRepository): ViewModel() {

    private val _allAddresses = MutableStateFlow<Resource<List<Address>>>(Resource.Undefined())
    val allAddresses = _allAddresses.asStateFlow()

    init {
        getAllAddresses()
    }

    private fun getAllAddresses() {
        viewModelScope.launch(Dispatchers.IO) {
            _allAddresses.value = Resource.Loading()
            addressesRepository.getAllAddresses(onSuccess = { allAddresses -> _allAddresses.value = Resource.Success(allAddresses) },
            onFailure = { gettingAllAddressesError -> _allAddresses.value = Resource.Error(gettingAllAddressesError) })
        }
    }
}