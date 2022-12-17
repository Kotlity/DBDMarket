package com.dbd.market.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val country: String,
    val city: String,
    val street: String,
    val type: String,
    val phoneNumber: String
): Parcelable
