package com.dbd.market.repositories.market.user_avatar

import com.dbd.market.room.database.UserAvatarDatabase
import com.dbd.market.room.entity.UserAvatarEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserAvatarRepositoryImplementation @Inject constructor(private val userAvatarDatabase: UserAvatarDatabase): UserAvatarRepository {

    override fun getAllUserAvatars(): Flow<List<UserAvatarEntity>> = userAvatarDatabase.userAvatarDao().getAllUserAvatars()
}