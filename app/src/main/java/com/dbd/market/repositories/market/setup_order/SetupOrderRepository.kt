package com.dbd.market.repositories.market.setup_order

import com.dbd.market.data.Address
import com.dbd.market.data.Order

interface SetupOrderRepository {

    fun getAllAddresses(onSuccess: (List<Address>) -> Unit, onFailure: (String) -> Unit)

    fun addAddress(address: Address, onSuccess: () -> Unit, onFailure: (String) -> Unit)

    fun deleteAllCartProductsFromCollection(onSuccess: () -> Unit, onFailure: (String) -> Unit)

    fun addSetupOrderToOrderCollection(order: Order, onSuccess: () -> Unit, onFailure: (String) -> Unit)
}