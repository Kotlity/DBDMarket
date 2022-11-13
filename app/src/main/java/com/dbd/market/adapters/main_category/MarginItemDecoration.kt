package com.dbd.market.adapters.main_category

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val spaceSize: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = spaceSize;
        }
        with(outRect) {
            left = spaceSize
            right = spaceSize
            top = spaceSize
            bottom = spaceSize
        }
    }
}