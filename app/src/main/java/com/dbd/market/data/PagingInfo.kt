package com.dbd.market.data

data class PagingInfo(var pageNumber: Long = 1L, var oldProducts: List<Product>? = null, var isEndOfPaging: Boolean = false)
