package com.dbd.market.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Int,
    val discount: Float? = null,
    val size: String,
    val images: List<String>,
    val amount: Int
): Parcelable {
    constructor(): this("", "", "", "", 0, size = "", images = emptyList(), amount = 0)
}
