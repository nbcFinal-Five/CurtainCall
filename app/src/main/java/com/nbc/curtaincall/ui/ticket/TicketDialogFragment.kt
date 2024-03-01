package com.nbc.curtaincall.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.databinding.SimpleInfoBottomsheetDialogBinding
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.curtaincall.ui.main.MainViewModel
import com.nbc.curtaincall.ui.main.MainViewModelFactory

class TicketDialogFragment : BottomSheetDialogFragment() {
    private var _binding: SimpleInfoBottomsheetDialogBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MainViewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory(
            fetchRemoteRepository = FetchRepositoryImpl(fetch = fetch)
        )
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
        //TODO Swipe To Action
        with(sharedViewModel) {
            //HomeFragment에서 id를 보내주면 fetchShowDetail() 호출
            showId.observe(viewLifecycleOwner) { id ->
                sharedViewModel.fetchShowDetail(id)
            }
            showDetailInfo.observe(viewLifecycleOwner) { showDetailInfoList ->
                with(binding) {
                    ivDetailPosterImage.load(showDetailInfoList[0].poster)
                    tvDetailAge.text = showDetailInfoList[0].prfage
                    tvDetailRuntime.text = showDetailInfoList[0].prfruntime
                    tvDetailFacilityName.text = showDetailInfoList[0].dtguidance
                    tvDetailPrice.text= showDetailInfoList[0].pcseguidance
                    tvDetailFacilityName.text = showDetailInfoList[0].fcltynm
                    tvDetailGenre.text = showDetailInfoList[0].genrenm
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}