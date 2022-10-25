package com.dbd.market.utils

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dbd.market.R

fun Fragment.setUnderlineToLinkTextView(charSequence: String, textView: TextView) {
    val spannableString = SpannableString(charSequence)
    spannableString.setSpan(UnderlineSpan(), resources.getInteger(R.integer.zero), charSequence.length, resources.getInteger(R.integer.zero))
    textView.text = spannableString
}