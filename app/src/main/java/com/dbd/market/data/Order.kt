package com.dbd.market.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Order(
    val id: Int,
    val cartProductsSetupOrder: CartProductsSetupOrder,
    val address: Address,
    val time: Date
): Parcelable {

    constructor(): this(0, CartProductsSetupOrder(emptyList(), 0), Address(0, "", "", "", "", "", "", ""), Date())
}
