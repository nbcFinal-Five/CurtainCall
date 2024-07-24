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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpObserve() {
        viewModel.detailInfoList.observe(viewLifecycleOwner) {
            val firstShowDetail = it?.first()
            if (firstShowDetail != null) {
                with(binding) {
                    Glide.with(requireContext()).load(firstShowDetail.poster)
                        .override(Target.SIZE_ORIGINAL).into(ivDetailPoster)
                    tvDetailShowTitle.text = firstShowDetail.prfnm
                    tvDetailGenre.text = firstShowDetail.genrenm
                    tvDetailAgeSub.text = firstShowDetail.prfage
                    tvDetailPriceSub.text = firstShowDetail.pcseguidance
                    tvDetailShowState.text = firstShowDetail.prfstate
                    tvDetailPlace.text = firstShowDetail.fcltynm
                    tvDetailPeriod.text =
                        "${firstShowDetail.prfpdfrom} ~ ${firstShowDetail.prfpdto}"
                    tvDetailTimeSub.text = firstShowDetail.dtguidance
                    tvDetailCastSub.text =
                        if (firstShowDetail.prfcast.isNullOrBlank()) "미상" else firstShowDetail.prfcast
                    tvDetailProductSub.text =
                        if (firstShowDetail.entrpsnm.isNullOrBlank()) "미상" else firstShowDetail.entrpsnm
                }
            }
        }

        viewModel.detailInfoList.observe(viewLifecycleOwner) {
            val firstShowDetail = it!!.first()

            val id = firstShowDetail.mt20id
        }
    }
}
