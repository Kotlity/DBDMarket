package com.dbd.market.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProductsSetupOrder(val cartProductList: List<CartProduct>, val totalPrice: Int): Parcelable
