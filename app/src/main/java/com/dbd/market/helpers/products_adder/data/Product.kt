package com.dbd.market.helpers.products_adder.data

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Int,
    val discount: Float? = null,
    val size: List<String>
)
