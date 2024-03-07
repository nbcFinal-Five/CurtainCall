package com.nbc.shownect.ui.detail_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.nbc.shownect.databinding.ActivityDetailBinding
import com.nbc.shownect.ui.UserViewModel
import com.nbc.shownect.ui.detail_activity.adapter.DetailPagerAdapter
import com.nbc.shownect.util.Constants

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by lazy { ViewModelProvider(this)[DetailViewModel::class.java] }
    private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

    private val adapter: DetailPagerAdapter by lazy { DetailPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //뷰 페이저 탭 연결
        with(binding) {
            detailViewPager.adapter = adapter
            TabLayoutMediator(tabLayoutDetail, detailViewPager) { tab, position ->
                detailViewPager.currentItem = tab.position
                tab.text = when (position) {
                    0 -> "상세 정보"
                    1 -> "소개 이미지"
                    2 -> "공연장 위치"
                    3 -> "기대평/리뷰"
                    else -> ""
                }
            }.attach()
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //TicketFragment에서 공연 id,공연장 id 받아 옴
        val showId = intent.getStringExtra(Constants.SHOW_ID)
        val facilityId = intent.getStringExtra(Constants.FACILITY_ID)
        if (showId != null && facilityId != null) {
            detailViewModel.sharedId(showId, facilityId)
        }
    }
}
