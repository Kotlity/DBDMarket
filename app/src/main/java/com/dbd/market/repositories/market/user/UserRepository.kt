package com.dbd.market.repositories.market.user

import android.net.Uri
import com.dbd.market.data.User

interface UserRepository {

    fun getUser(onSuccess: (User) -> Unit, onFailure: (String) -> Unit)

    fun updateUserImage(imageUri: Uri, imageName: String, onSuccess: (Uri) -> Unit, onFailure: (String) -> Unit)
}