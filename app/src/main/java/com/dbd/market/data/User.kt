package com.dbd.market.data

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val image: String = ""
) {
    constructor(): this("", "", "", "")
}

