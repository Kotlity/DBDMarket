package com.dbd.market.utils

import android.net.Uri
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment


fun Fragment.retrieveFileName(fileUri: Uri): String {
    val cursor = requireActivity().contentResolver.query(fileUri, null, null, null, null)!!
    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor.moveToFirst()
    val fileName = cursor.getString(nameIndex)
    cursor.close()
    return fileName
}