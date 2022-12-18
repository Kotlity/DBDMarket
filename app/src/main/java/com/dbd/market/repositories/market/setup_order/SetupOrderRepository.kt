package com.dbd.market.repositories.market.setup_order

import com.dbd.market.data.Address

interface SetupOrderRepository {

    fun getAllAddresses(onSuccess: (List<Address>) -> Unit, onFailure: (String) -> Unit)

    fun addAddress(address: Address, onSuccess: () -> Unit, onFailure: (String) -> Unit)

}