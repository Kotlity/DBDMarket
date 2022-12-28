package com.dbd.market.repositories.market.user

import com.dbd.market.data.User

interface UserRepository {

    fun getUser(onSuccess: (User) -> Unit, onFailure: (String) -> Unit)
}