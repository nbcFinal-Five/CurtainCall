package com.nbc.curtaincall.ui.ticket

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.SimpleInfoBottomsheetDialogBinding
import com.nbc.curtaincall.ui.detail_activity.DetailActivity
import com.nbc.curtaincall.ui.detail_activity.DetailViewModel
import com.nbc.curtaincall.ui.main.MainViewModel
import com.nbc.curtaincall.util.Constants
import jp.wasabeef.glide.transformations.BlurTransformation

class TicketDialogFragment : BottomSheetDialogFragment() {
    private val detailViewModel: DetailViewModel by activityViewModels<DetailViewModel>()

    private var _binding: SimpleInfoBottomsheetDialogBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MainViewModel by activityViewModels<MainViewModel>()
    private var ticketId = ""
    private var facilityId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SimpleInfoBottomsheetDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        with(sharedViewModel) {
            //HomeFragment에서 id를 보내주면 fetchShowDetail() 호출
            showId.observe(viewLifecycleOwner) { id ->
                sharedViewModel.fetchShowDetail(id)
                ticketId = id
            }

            showDetailInfo.observe(viewLifecycleOwner) {
                val showDetail = it?.first()
                if (showDetail != null) {
                    with(binding) {
                        tvSimpleShowTitle.text = showDetail.prfnm
                        tvSimpleAge.text = showDetail.prfage
                        tvSimplePlace.text = showDetail.fcltynm
                        tvSimpleGenre.text = showDetail.genrenm
                        tvSimpleShowState.text = showDetail.prfstate
                        tvSimpleCastSub.text =
                            if (showDetail.prfcast.isNullOrBlank()) "미상" else showDetail.prfcast
                        Glide.with(requireContext()).load(showDetail.poster)
                            .into(ivSimplePosterImage)
                        Glide.with(requireContext()).load(showDetail.poster)
                            .apply(
                                RequestOptions.bitmapTransform(
                                    BlurTransformation(20, 5)
                                )
                            ).into(ivSimplePosterBlur)
                    }
                    facilityId = showDetail.mt10id.toString()
                    Log.d("TAG", "onViewCreated: ${showDetail.relates?.relatesList}")
                }
            }
        }

        //Swipe Gesture
        binding.layoutSimpleScrollview.setOnTouchListener(object :
            OnSwipeTouchListener(requireContext()) {
            override fun onSwipeTop() {
                super.onSwipeTop()
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(Constants.SHOW_ID, ticketId)
                    putExtra(Constants.FACILITY_ID, facilityId)
                }
                activity?.overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
            }
        })

        binding.tvSimpleExpectationsNum.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(Constants.SHOW_ID, ticketId)
                putExtra(Constants.FACILITY_ID, facilityId)
                putExtra(Constants.IS_DIRECT, true)
            }
            activity?.overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = dialog as? BottomSheetDialog
        val bottomSheet =
            bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val displayMetrics = Resources.getSystem().displayMetrics
            // 화면 높이의 90%를 계산
            val desiredHeight = (displayMetrics.heightPixels * 0.9).toInt()
            val layoutParams = it.layoutParams
            layoutParams.height = desiredHeight
            it.layoutParams = layoutParams
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED // Bottom Sheet를 확장 상태로 시작
            behavior.peekHeight = desiredHeight // 필요한 경우 높이 제한 설정
        }
    }


    private fun initViewModel() {
        //  HomeFragment에서 id를 보내주면 fetchShowDetail() 호출
        sharedViewModel.showId.observe(viewLifecycleOwner) { id ->
            sharedViewModel.fetchShowDetail(id)
            ticketId = id
        }

        sharedViewModel.showDetailInfo.observe(viewLifecycleOwner) {
            val showDetail = it?.first()
            if (showDetail != null) {
                with(binding) {
                    tvSimpleShowTitle.text = showDetail.prfnm
                    tvSimpleAge.text = showDetail.prfage
                    tvSimplePlace.text = showDetail.fcltynm
                    tvSimpleGenre.text = showDetail.genrenm
                    tvSimpleShowState.text = showDetail.prfstate
                    tvSimpleCastSub.text =
                        if (showDetail.prfcast.isNullOrBlank()) "미상" else showDetail.prfcast
                }
                facilityId = showDetail.mt10id.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

