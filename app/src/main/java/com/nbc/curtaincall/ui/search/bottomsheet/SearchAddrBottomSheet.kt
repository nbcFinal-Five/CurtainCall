package com.nbc.curtaincall.ui.search.bottomsheet

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.SearchBottomsheetDialogAddrBinding
import com.nbc.curtaincall.ui.search.SearchListAdapter
import com.nbc.curtaincall.ui.search.SearchViewModel

class SearchAddrBottomSheet : BottomSheetDialogFragment() {

    private var _binding: SearchBottomsheetDialogAddrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchFilterViewModel  by activityViewModels<SearchViewModel>()
    private val addrFilterOptions by lazy {
        with(binding) {
            listOf(
                cpBottomAddrNationwide to "",
                cpBottomAddrSeoul to "11",
                cpBottomAddrIncheon to "28",
                cpBottomAddrDaejeon to "30",
                cpBottomAddrDaegu to "27",
                cpBottomAddrGwangju to "29",
                cpBottomAddrPusan to "26",
                cpBottomAddrUlsan to "31",
                cpBottomAddrSejong to "36",
                cpBottomAddrGyeonggi to "41",
                cpBottomAddrChungcheong to "43|44",
                cpBottomAddrGyeongsang to "47|48",
                cpBottomAddrJeolla to "45|46",
                cpBottomAddrGangwon to "51",
                cpBottomAddrJeju to "50",
                cpBottomAddrDaehakro to "UNI"
            )
        }
    }
    private var selectedChips : List<Chip>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = SearchBottomsheetDialogAddrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       clickFilterButton()

    }
    private fun clickFilterButton() {
        with(binding) {
            cpGroupAddr.setOnCheckedStateChangeListener { group, checkedIds ->
                selectedChips  = checkedIds.map { group.findViewById<Chip>(it)}
                    if(checkedIds.size > 0) {
                    btnAddrCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                    btnAddrCheck.setTypeface(null, Typeface.BOLD)
                    btnAddrCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
                    } else {
                    btnAddrCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_text_color))
                    btnAddrCheck.setTypeface(null, Typeface.NORMAL)
                    btnAddrCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_color))
                }
            }

            ivAddrFilterClose.setOnClickListener {
                dismiss()
            }

            btnAddrCheck.setOnClickListener {
                // 칩 그룹에서 칩 찾는법
                // 필터, 인덱스
                // 각각 똑같은것을 찾아서 아이디를 받기

               val selectedResult =  selectedChips?.map { chip -> addrFilterOptions.find { chip == it.first } }
                searchFilterViewModel.getAddrFilteredList(selectedResult)// 선택된 filter 뷰모델에 전달
//                searchFilterViewModel.fetchSearchFilterResult() // 뷰모델에서 넘겨받은 리스트를 통해 api 요청함수 실행
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "SearchAddrBottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}