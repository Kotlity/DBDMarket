package com.dbd.market.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.dbd.market.R

fun showToast(context: Context, viewGroup: ViewGroup, errorMessage: String) {
    val layout: View = LayoutInflater.from(context).inflate(R.layout.toast_custom_layout, viewGroup.findViewById(R.id.toastLinearLayout))

    layout.findViewById<TextView>(R.id.toastCustomTextView).text = errorMessage

    val toast = Toast(context)
    toast.apply {
        setGravity(Gravity.BOTTOM, 0, 80)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}