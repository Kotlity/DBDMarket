package com.dbd.market.repositories.market.orders

import com.dbd.market.data.Order

interface OrdersRepository {

    fun getAllOrders(onSuccess: (List<Order>) -> Unit, onFailure: (String) -> Unit)
}