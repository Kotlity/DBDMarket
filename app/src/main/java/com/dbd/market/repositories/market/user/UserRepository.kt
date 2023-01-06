package com.dbd.market.repositories.market.user

import android.net.Uri
import com.dbd.market.data.Order
import com.dbd.market.data.User
import com.dbd.market.room.entity.UserAvatarEntity

interface UserRepository {

    fun getUser(onSuccess: (User) -> Unit, onFailure: (String) -> Unit)

    fun uploadUserImageToFirebaseStorage(imageUri: Uri, imageName: String, onSuccess: (Uri) -> Unit, onFailure: (String) -> Unit)

    fun getUserRecentOrder(onSuccess: (Order) -> Unit, onFailure: (String) -> Unit)

    fun userLogout(onSuccess: () -> Unit, onFailure: (String) -> Unit )

    suspend fun insertUserAvatar(userAvatarEntity: UserAvatarEntity)
}