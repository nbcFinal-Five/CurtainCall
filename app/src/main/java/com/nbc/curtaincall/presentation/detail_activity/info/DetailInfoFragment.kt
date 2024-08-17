package com.nbc.curtaincall.ui.detail_activity.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentDetailDetailInfoBinding
import com.nbc.curtaincall.ui.detail_activity.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInfoFragment : Fragment(R.layout.fragment_detail_detail_info) {
    private var _binding: FragmentDetailDetailInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by activityViewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailDetailInfoBinding.inflate(inflater, container, false)
        setUpObserve()
        return binding.root
    }

    private fun setUpObserve() {
        viewModel.detailInfoList.observe(viewLifecycleOwner) {
            val firstShowDetail = it?.first()
            if (firstShowDetail != null) {
                with(binding) {
                    Glide.with(requireContext()).load(firstShowDetail.posterPath)
                        .override(Target.SIZE_ORIGINAL).into(ivDetailPoster)
                    tvDetailShowTitle.text = firstShowDetail.title
                    tvDetailGenre.text = firstShowDetail.genre
                    tvDetailAgeSub.text = firstShowDetail.age
                    tvDetailPriceSub.text = firstShowDetail.price
                    tvDetailShowState.text = firstShowDetail.showState
                    tvDetailPlace.text = firstShowDetail.placeName
                    tvDetailPeriod.text =
                        "${firstShowDetail.periodFrom} ~ ${firstShowDetail.periodTo}"
                    tvDetailTimeSub.text = firstShowDetail.time
                    tvDetailCastSub.text =
                        if (firstShowDetail.cast.isNullOrBlank()) "미상" else firstShowDetail.cast
                    tvDetailProductSub.text =
                        if (firstShowDetail.productCast.isNullOrBlank()) "미상" else firstShowDetail.productCast
                }
            }
        }

    }
}
