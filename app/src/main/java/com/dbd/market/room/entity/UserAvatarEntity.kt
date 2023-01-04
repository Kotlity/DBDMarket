package com.dbd.market.room.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserAvatarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userAvatar: Uri
)
