package com.dbd.market.utils

import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun <VH: RecyclerView.ViewHolder> Fragment.autoScrollRecyclerViewLogic(recyclerView: RecyclerView, recyclerViewAdapter: RecyclerView.Adapter<VH>, linearLayoutManager: LinearLayoutManager) {
    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < recyclerViewAdapter.itemCount - 1) {
        linearLayoutManager.smoothScrollToPosition(recyclerView, RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1)
    } else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerViewAdapter.itemCount - 1) {
        linearLayoutManager.smoothScrollToPosition(recyclerView, RecyclerView.State(), 0)
    }
}

fun Fragment.productRecyclerViewReachedBottomLogic(nestedScrollView: NestedScrollView, loadMoreData: () -> Unit) {
    nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { view, _, scrollY, _, _ ->
        if (view.getChildAt(0).bottom <= view.height + scrollY) loadMoreData()
    })
}

fun Fragment.productRecyclerViewReachedRightLogic(recyclerView: RecyclerView, loadMoreData: () -> Unit) {
    recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE) loadMoreData()
        }
    })
}

