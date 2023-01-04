package com.dbd.market.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dbd.market.room.converter.UserAvatarConverter
import com.dbd.market.room.dao.UserAvatarDao
import com.dbd.market.room.entity.UserAvatarEntity

@Database(entities = [UserAvatarEntity::class], version = 1)
@TypeConverters(UserAvatarConverter::class)
abstract class UserAvatarDatabase: RoomDatabase() {

    abstract fun userAvatarDao(): UserAvatarDao
}