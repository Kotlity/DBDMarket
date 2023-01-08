package com.dbd.market.repositories.market.search

import com.dbd.market.data.Product

interface SearchRepository {

    fun searchProducts(searchQuery: String, onSuccess: (List<Product>) -> Unit, onFailure: (String) -> Unit)
}