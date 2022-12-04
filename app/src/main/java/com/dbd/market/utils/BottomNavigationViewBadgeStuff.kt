package com.dbd.market.utils

import androidx.core.content.res.ResourcesCompat
import com.dbd.market.R
import com.google.android.material.bottomnavigation.BottomNavigationView

enum class BottomNavigationViewBadgeState {
    NOTZERO, ZERO, ERROR
}

fun BottomNavigationView.updateBottomNavigationViewBadge(bottomNavigationViewBadgeState: BottomNavigationViewBadgeState, fragmentId: Int, badgeCount: Int = 0) {
    when (bottomNavigationViewBadgeState) {
        BottomNavigationViewBadgeState.NOTZERO -> {
            getOrCreateBadge(fragmentId).apply {
                backgroundColor = resources.getColor(R.color.dark_yellow)
                number = badgeCount
            }
        }
        BottomNavigationViewBadgeState.ZERO -> {
            getOrCreateBadge(fragmentId).apply {
                backgroundColor = resources.getColor(R.color.red)
                number = badgeCount
            }
        }
        BottomNavigationViewBadgeState.ERROR -> {
            getOrCreateBadge(fragmentId).apply {
                background = ResourcesCompat.getDrawable(resources, R.drawable.ic_error_icon, null)
            }
        }
    }
}