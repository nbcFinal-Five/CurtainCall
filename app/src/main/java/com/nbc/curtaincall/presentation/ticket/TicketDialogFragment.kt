package com.nbc.curtaincall.ui.ticket

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.SimpleInfoBottomsheetDialogBinding
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.presentation.ticket.ReserveFragment
import com.nbc.curtaincall.ui.detail_activity.DetailActivity
import com.nbc.curtaincall.ui.main.MainViewModel
import com.nbc.curtaincall.util.Constants
import jp.wasabeef.glide.transformations.BlurTransformation

class TicketDialogFragment : BottomSheetDialogFragment() {

    private var _binding: SimpleInfoBottomsheetDialogBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MainViewModel by activityViewModels<MainViewModel>()
    private lateinit var mt20Id: String
    private lateinit var mt10Id: String
    private lateinit var reserveInfo: List<ShowItem.Relate>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SimpleInfoBottomsheetDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        with(sharedViewModel) {
            //HomeFragment에서 id를 보내주면 fetchShowDetail() 호출
            showId.observe(viewLifecycleOwner) { id ->
                sharedViewModel.fetchShowDetail(id)
            }

            showDetailInfo.observe(viewLifecycleOwner) {
                it?.first()?.let { showDetail ->
                    with(binding) {
                        tvSimpleShowTitle.text = showDetail.title
                        tvSimpleAge.text = showDetail.age
                        tvSimplePlace.text = showDetail.placeName
                        tvSimpleGenre.text = showDetail.genre
                        tvSimpleShowState.text = showDetail.showState
                        tvSimpleCastSub.text =
                            if (showDetail.cast.isNullOrBlank()) "미상" else showDetail.cast
                        Glide.with(requireContext()).load(showDetail.posterPath)
                            .into(ivSimplePosterImage)
                        Glide.with(requireContext()).load(showDetail.posterPath)
                            .apply(
                                RequestOptions.bitmapTransform(
                                    BlurTransformation(20, 5)
                                )
                            ).into(ivSimplePosterBlur)
                    }
                    mt10Id = showDetail.showId.toString()
                    mt20Id = showDetail.facilityId.toString()
                    reserveInfo = showDetail.relateList

                }
            }
            with(binding) {

                btnDetail.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.apply {
                        putExtra(Constants.SHOW_ID, mt10Id)
                        putExtra(Constants.FACILITY_ID, mt20Id)
                    }
                    startActivity(intent)
                    activity?.overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
                }
                btnReserve.setOnClickListener {
                    val reserveDialog = ReserveFragment()
                    reserveDialog.setStyle(
                        DialogFragment.STYLE_NORMAL,
                        R.style.RoundedBottomSheetDialog
                    )

                    sharedViewModel.shareReserveInfo(reserveInfo)

                    reserveDialog.show(childFragmentManager, reserveDialog.tag)
                }
            }
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
        }

        sharedViewModel.showDetailInfo.observe(viewLifecycleOwner) {
            val showDetail = it?.first()
            if (showDetail != null) {
                with(binding) {
                    tvSimpleShowTitle.text = showDetail.title
                    tvSimpleAge.text = showDetail.age
                    tvSimplePlace.text = showDetail.placeName
                    tvSimpleGenre.text = showDetail.genre
                    tvSimpleShowState.text = showDetail.showState
                    tvSimpleCastSub.text =
                        if (showDetail.cast.isNullOrBlank()) "미상" else showDetail.cast
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

