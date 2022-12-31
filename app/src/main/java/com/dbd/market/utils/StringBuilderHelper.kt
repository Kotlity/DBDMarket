package com.dbd.market.utils

fun formatString(text: String) = StringBuilder(text).insert(0, "\"").insert(text.length + 1, "\"").toString()