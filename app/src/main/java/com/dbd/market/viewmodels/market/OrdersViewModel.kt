package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.Order
import com.dbd.market.repositories.market.orders.OrdersRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val ordersRepository: OrdersRepository): ViewModel() {

    private val _orders = MutableStateFlow<Resource<List<Order>>>(Resource.Undefined())
    val orders = _orders.asStateFlow()

    init {
        getAllOrders()
    }

    private fun getAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            _orders.value = Resource.Loading()
            ordersRepository.getAllOrders(onSuccess = { sortedListOfOrder -> _orders.value = Resource.Success(sortedListOfOrder) },
                onFailure = { gettingSortedListOfOrderError -> _orders.value = Resource.Error(gettingSortedListOfOrderError)}
            )
        }
    }
}