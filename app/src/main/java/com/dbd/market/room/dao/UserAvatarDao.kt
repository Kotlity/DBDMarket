package com.dbd.market.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dbd.market.room.entity.UserAvatarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAvatarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAvatarEntity(userAvatarEntity: UserAvatarEntity)

    @Query("SELECT * FROM UserAvatarEntity ORDER BY id ASC")
    fun getAllUserAvatars(): Flow<List<UserAvatarEntity>>
}