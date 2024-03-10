package com.nbc.shownect.ui.home

import android.content.Context
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.nbc.shownect.R
import kotlin.math.abs

//페이지 변환 효과, 스크롤될 때 각 페이지에 적용되는 시각적 변환
class SliderTransformer(context: Context) :
    ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            translationX = -pageTranslationX * position
            scaleY = 1 - (0.25f * abs(position))
            alpha = 0.25f + (1 - abs(position))
        }

    }

    val nextItemVisiblePx =
        context.resources.getDimension(R.dimen.viewpager_next_item_visible)
    val currentItemHorizontalMarginPx =
        context.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
}