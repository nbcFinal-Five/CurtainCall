package com.nbc.curtaincall.ui.home

import android.content.Context
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.nbc.curtaincall.R
import kotlin.math.abs

class SliderTransformer(context: Context) :
    ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -pageTranslationX * position
        page.scaleY = 1 - (0.25f * abs(position))
    }

    val nextItemVisiblePx =
        context.resources.getDimension(R.dimen.viewpager_next_item_visible)
    val currentItemHorizontalMarginPx =
        context.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
}