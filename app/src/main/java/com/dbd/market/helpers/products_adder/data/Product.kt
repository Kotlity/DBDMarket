package com.dbd.market.helpers.products_adder.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Int,
    val discount: Float? = null,
    val size: List<String>,
    val images: List<String>
): Parcelable {
    constructor(): this("0", "", "", "", 0, size = emptyList(), images = emptyList())
}
