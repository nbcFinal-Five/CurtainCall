package com.nbc.shownect.ui.detail_activity.info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentDetailDetailInfoBinding
import com.nbc.shownect.supabase.Supabase
import com.nbc.shownect.ui.UserViewModel
import com.nbc.shownect.ui.auth.AuthActivity
import com.nbc.shownect.ui.detail_activity.DetailViewModel
import io.github.jan.supabase.gotrue.auth

class DetailInfoFragment : Fragment() {
    private var _binding: FragmentDetailDetailInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by activityViewModels<DetailViewModel>()
    private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            userViewModel.setUser()

            val user = userViewModel.userInfo.value

            val info = viewModel.detailInfoList.value?.first()

            if (info != null && user != null) {
                viewModel.setInfo(info.mt20id!!)
                viewModel.setIsLike(
                    mt20id = info.mt20id!!,
                    userId = user.id
                )
            }
        }


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
        viewModel.point.observe(viewLifecycleOwner) {
            binding.rbDetailBar.rating = it.toFloat()
        }

        viewModel.totalExpectationCount.observe(viewLifecycleOwner) {
            binding.tvDetailExpectationsNum.text = "기대평 ${it}개"
        }


        viewModel.isBookmark.observe(viewLifecycleOwner) {
            binding.ivDetailWishlist.setBackgroundResource(if (it) R.drawable.ic_heart_full_24dp else R.drawable.ic_heart_fill)
        }

        viewModel.isBookmark.observe(viewLifecycleOwner) {
            val info = viewModel.detailInfoList.value?.first()

            with(binding) {
                if (it) {
                    ivDetailWishlist.setBackgroundResource(R.drawable.ic_heart_full_24dp)
                    ivDetailWishlist.setOnClickListener {
                        val user = userViewModel.userInfo.value!!

                        viewModel.deleteBookmark(
                            mt20id = info?.mt20id!!,
                            userId = user.id,
                        )
                    }
                } else {
                    ivDetailWishlist.setBackgroundResource(R.drawable.ic_heart_fill)
                    ivDetailWishlist.setOnClickListener {
                        val user = userViewModel.userInfo.value

                        if (user == null) {
                            val intent = Intent(requireActivity(), AuthActivity::class.java)
                            launcher.launch(intent)
                            return@setOnClickListener
                        } else {
                            viewModel.createBookmark(
                                mt20id = info?.mt20id!!,
                                mt10id = info.mt10id!!,
                                poster = info.poster!!,
                                userId = user.id
                            )
                        }
                    }
                }
            }
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