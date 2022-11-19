package com.dbd.market.data

import com.dbd.market.helpers.products_adder.data.Product

data class PagingInfo(var pageNumber: Long = 1L, var oldProducts: List<Product>? = null, var isEndOfPaging: Boolean = false)
