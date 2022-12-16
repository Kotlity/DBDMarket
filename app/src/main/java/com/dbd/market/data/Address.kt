package com.dbd.market.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: Int,
    val type: String,
    val firstName: String,
    val lastName: String,
    val country: String,
    val city: String,
    val street: String,
    val phoneNumber: Long
): Parcelable
