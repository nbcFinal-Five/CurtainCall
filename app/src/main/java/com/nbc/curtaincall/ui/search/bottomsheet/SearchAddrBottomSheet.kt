package com.nbc.curtaincall.ui.search.bottomsheet

import android.graphics.Typeface
import android.os.Bundle
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
    private val searchListAdapter by lazy { SearchListAdapter() }
    private val addrFilterOptions by lazy {
        listOf(
            listOf(
                with(binding){
                    cpBottomAddrNationwide to ""
                    cpBottomAddrSeoul to 11
                    cpBottomAddrIncheon to 28
                    cpBottomAddrDaejeon to 30
                    cpBottomAddrDaegu to 27
                    cpBottomAddrGwangju to 29
                    cpBottomAddrPusan to 26
                    cpBottomAddrUlsan to 31
                    cpBottomAddrSejong to 36
                    cpBottomAddrGyeonggi to 41
                    cpBottomAddrChungcheong to 4344
                    cpBottomAddrGyeongsang to 4748
                    cpBottomAddrJeolla to 4546
                    cpBottomAddrGangwon to 51
                    cpBottomAddrJeju to 50
                    cpBottomAddrDaehakro to "UNI"
                }
            )
        )
    }
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
            cpGroupAddr.setOnCheckedChangeListener{group, checkedId ->
                var isAnyChipSelected = false

                // 선택된 모든 칩을 확인합니다.
                for (i in 0 until cpGroupAddr.childCount) {
                    val chip = cpGroupAddr.getChildAt(i) as Chip

                    if (chip.isChecked) {
                        isAnyChipSelected = true
                        break
                    }
                }

                if (isAnyChipSelected) {
                    btnAddrCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                    btnAddrCheck.setTypeface(null, Typeface.BOLD)
                    btnAddrCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
                } else {
                    btnAddrCheck?.setTextColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_text_color))
                    btnAddrCheck?.setTypeface(null, Typeface.NORMAL)
                    btnAddrCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_color))
                }
            }

            ivAddrFilterClose.setOnClickListener {
                dismiss()
            }

            btnAddrCheck.setOnClickListener {
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