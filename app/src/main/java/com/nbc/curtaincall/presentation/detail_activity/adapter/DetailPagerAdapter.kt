package com.nbc.curtaincall.ui.detail_activity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nbc.curtaincall.presentation.detail_activity.review.ReviewFragment
import com.nbc.curtaincall.ui.detail_activity.image.IntroImageFragment
import com.nbc.curtaincall.ui.detail_activity.info.DetailInfoFragment
import com.nbc.curtaincall.ui.detail_activity.location.LocationFragment

class DetailPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DetailInfoFragment()
            1 -> IntroImageFragment()
            2 -> LocationFragment()
            3 -> ReviewFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }
}