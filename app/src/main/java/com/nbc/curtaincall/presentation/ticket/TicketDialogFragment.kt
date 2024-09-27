package com.nbc.curtaincall.ui.ticket

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.SimpleInfoBottomsheetDialogBinding
import com.nbc.curtaincall.presentation.TicketViewModel
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.ui.detail_activity.DetailActivity
import com.nbc.curtaincall.util.Constants
import com.nbc.curtaincall.util.LoginDialogFragment
import com.nbc.curtaincall.util.ViewUtil.setBackGround
import com.nbc.curtaincall.util.ViewUtil.setPoster
import kotlinx.coroutines.launch

class TicketDialogFragment : BottomSheetDialogFragment() {
    private var _binding: SimpleInfoBottomsheetDialogBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val ticketViewModel: TicketViewModel by activityViewModels<TicketViewModel>()
    private lateinit var mt20Id: String
    private lateinit var mt10Id: String
    private lateinit var reserveInfo: List<ShowItem.Relate>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SimpleInfoBottomsheetDialogBinding.inflate(inflater, container, false)
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListener()
    }

    private fun setUpClickListener() {
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
                ticketViewModel.shareReserveInfo(reserveInfo, childFragmentManager)
            }
            aroundBookmark.setOnClickListener {
                lifecycleScope.launch {
                    ticketViewModel.isLogin.collect { state ->
                        if (state) {
                            ticketViewModel.bookMark(mt10Id)
                        } else {
                            LoginDialogFragment().show(
                                requireActivity().supportFragmentManager,
                                "loginDialog"
                            )
                        }
                    }
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
        with(ticketViewModel) {
            showDetailInfo.observe(viewLifecycleOwner) {
                it?.first()?.let { showDetail ->
                    binding.apply {
                        tvSimpleShowTitle.text = showDetail.title
                        tvSimpleAge.text = showDetail.age
                        tvSimplePlace.text = showDetail.placeName
                        tvSimpleGenre.text = showDetail.genre
                        tvSimpleShowState.text = showDetail.showState
                        tvSimpleCastSub.text =
                            if (showDetail.cast.isNullOrBlank()) "미상" else showDetail.cast
                        ivSimplePosterImage.setPoster(
                            ivSimplePosterImage,
                            showDetail.posterPath
                        )
                        ivSimplePosterBlur.setBackGround(
                            requireContext(),
                            showDetail.posterPath
                        )
                        tvSimpleArea.text = showDetail.area
                        ivSimpleBookmark.setImageResource(if (showDetail.isBookmarked) R.drawable.ic_heart_full_24dp else R.drawable.ic_heart_empty_24dp)
                    }
                    mt10Id = showDetail.showId.toString()
                    mt20Id = showDetail.facilityId.toString()
                    reserveInfo = showDetail.relateList ?: emptyList()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::mt10Id.isInitialized) {
            lifecycleScope.launch {
                ticketViewModel.isLogin.collect { state ->
                    if (state) {
                        val isBookmarked = ticketViewModel.checkBookmark(mt10Id)
                        binding.ivSimpleBookmark.setImageResource(if (isBookmarked) R.drawable.ic_heart_full_24dp else R.drawable.ic_heart_empty_24dp)
                    } else {
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

