package com.dbd.market.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val type: String, private val spaceSize: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            when (type) {
                Constants.MARGIN_ITEM_DECORATION_SIZE -> {
                    left = spaceSize
                    right = spaceSize
                }
                Constants.MARGIN_ITEM_DECORATION_PRODUCT -> {
                    if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) { outRect.bottom = spaceSize; }
                    left = spaceSize
                    right = spaceSize
                    top = spaceSize
                    bottom = spaceSize
                }
            }
        }
    }
}