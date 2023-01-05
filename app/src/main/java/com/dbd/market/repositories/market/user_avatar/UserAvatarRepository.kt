package com.dbd.market.repositories.market.user_avatar

import com.dbd.market.room.entity.UserAvatarEntity
import kotlinx.coroutines.flow.Flow

interface UserAvatarRepository {

    fun getAllUserAvatars(): Flow<List<UserAvatarEntity>>
}