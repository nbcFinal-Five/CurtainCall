package com.nbc.curtaincall.ui.ticket

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.databinding.SimpleInfoBottomsheetDialogBinding
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.curtaincall.ui.detail.DetailActivity
import com.nbc.curtaincall.ui.main.MainViewModel
import com.nbc.curtaincall.ui.main.MainViewModelFactory

class TicketDialogFragment : BottomSheetDialogFragment() {
    private var _binding: com.nbc.curtaincall.databinding.SimpleInfoBottomsheetDialogBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MainViewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory(
            fetchRemoteRepository = FetchRepositoryImpl(fetch = fetch)
        )
    }
    private var ticketId = ""

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
        with(sharedViewModel) {
            //HomeFragment에서 id를 보내주면 fetchShowDetail() 호출
            showId.observe(viewLifecycleOwner) { id ->
                sharedViewModel.fetchShowDetail(id)
                ticketId = id
            }
            showDetailInfo.observe(viewLifecycleOwner) { showDetailInfoList ->
                with(binding) {
                    tvSimpleShowNameSub.text = showDetailInfoList[0].prfnm
                    ivSimplePosterImage.load(showDetailInfoList[0].poster)
                    tvSimpleAge.text = showDetailInfoList[0].prfage
                    tvSimpleRuntimeSub.text = showDetailInfoList[0].prfruntime
                    tvSimplePlaceSub.text = showDetailInfoList[0].dtguidance
                    tvSimplePriceSub.text = showDetailInfoList[0].pcseguidance
                    tvSimplePlaceSub.text = showDetailInfoList[0].fcltynm
                    tvSimpleGenre.text = showDetailInfoList[0].genrenm
                    tvSimpleShowState.text = showDetailInfoList[0].prfstate
                    tvSimpleCastSub.text =
                        if (showDetailInfoList[0].prfcast.isNullOrEmpty()) "미상" else showDetailInfoList[0].prfcast
                    tvSimpleProductSub.text =
                        if (showDetailInfoList[0].entrpsnm.isNullOrEmpty()) "미상" else showDetailInfoList[0].entrpsnm

                }

            }
        }
        binding.btnToDetailActivity.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("fromTicketId",ticketId)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}