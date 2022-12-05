package com.dbd.market.utils

import androidx.core.content.res.ResourcesCompat
import com.dbd.market.R
import com.google.android.material.bottomnavigation.BottomNavigationView

enum class BottomNavigationViewBadgeState {
    NOTZERO, ZERO, ERROR
}

fun BottomNavigationView.updateBottomNavigationViewBadge(bottomNavigationViewBadgeState: BottomNavigationViewBadgeState, badgeCount: Int = 0) {
    when (bottomNavigationViewBadgeState) {
        BottomNavigationViewBadgeState.NOTZERO -> { createBadge(resources.getColor(R.color.dark_yellow), badgeCount) }
        BottomNavigationViewBadgeState.ZERO -> { createBadge(resources.getColor(R.color.red), badgeCount) }
        BottomNavigationViewBadgeState.ERROR -> {
            getOrCreateBadge(R.id.cartFragment).apply {
                background = ResourcesCompat.getDrawable(resources, R.drawable.ic_error_icon, null)
            }
        }
    }
}

private fun BottomNavigationView.createBadge(color: Int, badgeCount: Int) {
    getOrCreateBadge(R.id.cartFragment).apply {
        backgroundColor = color
        number = badgeCount
    }
}