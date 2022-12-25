package com.dbd.market.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun convertDateToString(date: Date): String { return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) }