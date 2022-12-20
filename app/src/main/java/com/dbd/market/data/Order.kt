package com.dbd.market.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val cartProductsSetupOrder: CartProductsSetupOrder,
    val address: Address,
    val time: String
): Parcelable {

    constructor(): this(CartProductsSetupOrder(emptyList(), 0), Address(0, "", "", "", "", "", "", ""), "")
}
