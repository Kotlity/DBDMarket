package com.dbd.market.utils

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dbd.market.R

fun showToast(context: Context, viewGroup: ViewGroup,image: Int, message: String) {
    val layout: View = LayoutInflater.from(context).inflate(R.layout.toast_custom_layout, viewGroup.findViewById(R.id.toastLinearLayout))

    layout.findViewById<ImageView>(R.id.toastCustomImageView).setImageResource(image)
    layout.findViewById<TextView>(R.id.toastCustomTextView).text = message

    val toast = Toast(context)
    toast.apply {
        setGravity(Gravity.BOTTOM, 0, 80)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}