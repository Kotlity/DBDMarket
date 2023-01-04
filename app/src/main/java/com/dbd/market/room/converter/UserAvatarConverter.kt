package com.dbd.market.room.converter

import android.net.Uri
import androidx.room.TypeConverter


class UserAvatarConverter {

    @TypeConverter
    fun fromUriToString(uri: Uri?): String? { return uri?.toString() }

    @TypeConverter
    fun fromStringToUri(string: String?): Uri? { return if (string == null) null else Uri.parse(string) }
}