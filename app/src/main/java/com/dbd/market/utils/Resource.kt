package com.dbd.market.utils

sealed class Resource<T>(private val data: T? = null, private val message: String? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String): Resource<T>(message = message)
    class Loading<T>(): Resource<T>()
}
