package com.nbc.shownect.ui.detail_activity.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.size.Size
import coil.size.SizeResolver
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentDetailDetailInfoBinding
import com.nbc.shownect.supabase.Supabase
import com.nbc.shownect.ui.detail_activity.DetailViewModel
import io.github.jan.supabase.gotrue.auth

class DetailInfoFragment : Fragment() {
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
        viewModel.fetchDetailInfo()
    }

    private fun setUpObserve() {
        viewModel.detailInfoList.observe(viewLifecycleOwner) {
            val firstShowDetail = it?.first()
            if (firstShowDetail != null) {
                with(binding) {
                    ivDetailPoster.load(firstShowDetail.poster) {
                        size(resolver = SizeResolver(Size.ORIGINAL))
                    }
                    tvDetailShowNameSub.text = firstShowDetail.prfnm
                    tvDetailGenreSub.text = firstShowDetail.genrenm
                    tvDetailAgeSub.text = firstShowDetail.prfage
                    tvDetailRuntimeSub.text = firstShowDetail.prfruntime
                    tvDetailPriceSub.text = firstShowDetail.pcseguidance
                    tvDetailShowStateSub.text = firstShowDetail.prfstate
                    tvDetailPlaceSub.text = firstShowDetail.fcltynm
                    tvDetailPeriodSub.text =
                        "${firstShowDetail.prfpdfrom} ~ ${firstShowDetail.prfpdto}"
                    tvDetailTimeSub.text = firstShowDetail.dtguidance
                    tvDetailCastSub.text =
                        if (firstShowDetail.prfcast.isNullOrBlank()) "미상" else firstShowDetail.prfcast
                    tvDetailProductSub.text =
                        if (firstShowDetail.entrpsnm.isNullOrBlank()) "미상" else firstShowDetail.entrpsnm
                }
            }
        }
        viewModel.point.observe(viewLifecycleOwner) {
            binding.rbDetailBar.rating = it.toFloat()
        }

        viewModel.totalExpectationCount.observe(viewLifecycleOwner) {
            binding.tvDetailExpectationsNum.text = "기대평 ${it}개"
        }

        viewModel.isBookmark.observe(viewLifecycleOwner) {
            binding.ivDetailWishlist.setBackgroundResource(if (it) R.drawable.ic_heart_full_24dp else R.drawable.ic_heart_empty_24dp)
        }

        viewModel.detailInfoList.observe(viewLifecycleOwner) {
            val firstShowDetail = it!!.first()

            val id = firstShowDetail.mt20id
            if (id != null) {
                viewModel.setInfo(id)

                val user = Supabase.client.auth.currentUserOrNull()

                if (user != null) {
                    viewModel.setIsLike(id, user.id)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}