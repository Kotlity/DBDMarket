package com.dbd.market.repositories.market.addresses

import com.dbd.market.data.Address

interface AddressesRepository {

    fun getAllAddresses(onSuccess: (List<Address>) -> Unit, onFailure: (String) -> Unit)
}