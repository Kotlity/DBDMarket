package com.dbd.market.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val marginItemDecorationType: MarginItemDecorationType, private val spaceSize: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            when (marginItemDecorationType) {
                MarginItemDecorationType.SIZE -> {
                    left = spaceSize
                    right = spaceSize
                }
                MarginItemDecorationType.PRODUCT -> {
                    if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) { outRect.bottom = spaceSize; }
                    left = spaceSize
                    right = spaceSize
                    top = spaceSize
                    bottom = spaceSize
                }
                MarginItemDecorationType.CARTPRODUCT -> {
                    top = spaceSize
                    bottom = spaceSize
                }
            }
        }
    }
}