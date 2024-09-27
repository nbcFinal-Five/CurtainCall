package com.nbc.curtaincall.ui.detail_activity.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentDetailDetailInfoBinding
import com.nbc.curtaincall.ui.detail_activity.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInfoFragment :
    Fragment(R.layout.fragment_detail_detail_info) {
    private var _binding: FragmentDetailDetailInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var showId: String
    private val detailViewModel by activityViewModels<DetailViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailDetailInfoBinding.inflate(inflater, container, false)
        setUpObserve()
        return binding.root
    }

    private fun setUpClickListener() = with(binding) {
        bookmarkContainer.setOnClickListener {
            if (Firebase.auth.currentUser != null) {
                detailViewModel.bookmark(showId)
            } else {
                Toast.makeText(requireContext(), "로그인이필요한 서비스입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListener()
        detailViewModel.fetchDetailInfo()
    }

    private fun setUpObserve() {
        detailViewModel.detailInfoList.observe(viewLifecycleOwner) {
            val detailInfo = it.firstOrNull()
            with(binding) {
                detailInfo?.let { detailInfo ->
                    Glide.with(requireContext()).load(detailInfo.posterPath)
                        .override(Target.SIZE_ORIGINAL).into(ivDetailPoster)
                    tvDetailShowTitle.text = detailInfo.title
                    tvDetailGenre.text = detailInfo.genre
                    tvDetailAgeSub.text = detailInfo.age
                    tvDetailPriceSub.text = detailInfo.price
                    tvDetailShowState.text = detailInfo.showState
                    tvDetailPlace.text = detailInfo.placeName
                    tvDetailAreaSub.text = detailInfo.area
                    tvDetailRuntimeSub.text = detailInfo.runTime
                    tvDetailPeriod.text =
                        "${detailInfo.periodFrom} ~ ${detailInfo.periodTo}"
                    tvDetailTimeSub.text = detailInfo.time
                    tvDetailCastSub.text =
                        if (detailInfo.cast.isNullOrBlank()) "미상" else detailInfo.cast
                    tvDetailProductSub.text =
                        if (detailInfo.productCast.isNullOrBlank()) "미상" else detailInfo.productCast
                    showId = detailInfo.showId.toString()
                    ivDetailBookmark.setImageResource(if (detailInfo.isBookmarked) R.drawable.ic_heart_full_24dp else R.drawable.ic_heart_empty_24dp)
                }
            }
        }
    }
}
