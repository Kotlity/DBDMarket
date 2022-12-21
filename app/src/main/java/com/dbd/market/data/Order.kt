package com.dbd.market.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: Int,
    val cartProductsSetupOrder: CartProductsSetupOrder,
    val address: Address,
    val time: String
): Parcelable {

    constructor(): this(0, CartProductsSetupOrder(emptyList(), 0), Address(0, "", "", "", "", "", "", ""), "")
}
