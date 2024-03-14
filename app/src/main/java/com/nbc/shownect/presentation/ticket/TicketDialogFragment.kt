package com.nbc.shownect.ui.ticket

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.shownect.R
import com.nbc.shownect.databinding.SimpleInfoBottomsheetDialogBinding
import com.nbc.shownect.fetch.model.DbResponse
import com.nbc.shownect.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.shownect.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.shownect.supabase.Supabase
import com.nbc.shownect.ui.UserViewModel
import com.nbc.shownect.ui.auth.AuthActivity
import com.nbc.shownect.ui.detail_activity.DetailActivity
import com.nbc.shownect.ui.detail_activity.DetailViewModel
import com.nbc.shownect.ui.main.MainViewModel
import com.nbc.shownect.ui.main.MainViewModelFactory
import com.nbc.shownect.util.Constants
import io.github.jan.supabase.gotrue.auth
import jp.wasabeef.glide.transformations.BlurTransformation

class TicketDialogFragment : BottomSheetDialogFragment() {
    private val detailViewModel: DetailViewModel by activityViewModels<DetailViewModel>()
    private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }


    // activity 닫혔을 때 갱신용 launcher
    private var info: DbResponse? = null
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            userViewModel.setUser()

            val user = Supabase.client.auth.currentUserOrNull()

            if (info != null && user != null) {
                detailViewModel.setInfo(info!!.mt20id!!)
                detailViewModel.setIsLike(
                    mt20id = info!!.mt20id!!,
                    userId = user.id
                )
            }
        }


    private var _binding: SimpleInfoBottomsheetDialogBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MainViewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory(
            fetchRemoteRepository = FetchRepositoryImpl(fetch = fetch)
        )
    }
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
                launcher.launch(intent)
                activity?.overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
            }
        })
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
                info = showDetail

                val id = showDetail.mt20id
                val user = userViewModel.userInfo.value

                if (id != null) {
                    detailViewModel.setInfo(id)
                    detailViewModel.setIsLike(
                        mt20id = id,
                        userId = user?.id
                    )
                }

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

        detailViewModel.point.observe(viewLifecycleOwner) {
            binding.rbSimpleBar.rating = it.toFloat()
        }

        detailViewModel.totalExpectationCount.observe(viewLifecycleOwner) {
            binding.tvSimpleExpectationsNum.text = "기대평 ${it}개"
        }

        detailViewModel.isBookmark.observe(viewLifecycleOwner) {
            val info = sharedViewModel.showDetailInfo.value?.first()

            with(binding) {
                if (it) {
                    ivSimpleWishlist.setBackgroundResource(R.drawable.ic_heart_full_24dp)
                    ivSimpleWishlist.setOnClickListener {
                        val user = userViewModel.userInfo.value!!

                        detailViewModel.deleteBookmark(
                            mt20id = info?.mt20id!!,
                            userId = user.id,
                        )
                    }
                } else {
                    ivSimpleWishlist.setBackgroundResource(R.drawable.ic_heart_empty_24dp)
                    ivSimpleWishlist.setOnClickListener {
                        val user = userViewModel.userInfo.value

                        if (user == null) {
                            val intent = Intent(requireActivity(), AuthActivity::class.java)
                            launcher.launch(intent)
                            return@setOnClickListener
                        } else {
                            detailViewModel.createBookmark(
                                mt20id = info?.mt20id!!,
                                mt10id = info?.mt10id!!,
                                poster = info.poster!!,
                                userId = user.id
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

