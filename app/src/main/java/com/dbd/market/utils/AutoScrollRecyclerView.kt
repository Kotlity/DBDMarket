package com.dbd.market.utils

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

