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
import com.nbc.curtaincall.databinding.SearchBottomsheetDialogGenreBinding
import com.nbc.curtaincall.ui.search.SearchViewModel

class SearchGenreBottomSheet(private val previouslySelectedGenreChips: List<Int>?,
                             private val chipClickListener: (List<Int>) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: SearchBottomsheetDialogGenreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchFilterViewModel  by activityViewModels<SearchViewModel>()
    private val genreFilterOptions by lazy {
        with(binding){
            listOf(
                cpBottomCircus to "EEEB",
                cpBottomClassic to "CCCA",
                cpBottomFutility to "BBBC",
                cpBottomMix to "EEEA",
                cpBottomMusical to "GGGA",
                cpBottomKoreanClassic to "CCCC",
                cpBottomPopularMusic to "CCCD",
                cpBottomPublicfutility to "BBBE",
                cpBottomTheater to "AAAA"
            )
        }
    }
    private var selectedGenreChips : List<Chip>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = SearchBottomsheetDialogGenreBinding.inflate(inflater, container, false)
        restorePreviouslySelectedGenreChips()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       clickFilterButton()

    }

    private fun clickFilterButton() {
        with(binding) {
            cpGroupGenre.setOnCheckedStateChangeListener { group, checkedIds ->
                val selectedChips = checkedIds.toList()
                chipClickListener(selectedChips)
                selectedGenreChips = checkedIds.map { group.findViewById<Chip>(it)}
                // 칩 선택유무에 따라 조건검색 버튼 색상 변경
                if(checkedIds.isNotEmpty()) {
                    btnGenreCheck.isEnabled = true
                    btnGenreCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                    btnGenreCheck.setTypeface(null, Typeface.BOLD)
                    btnGenreCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
                } else {
                    btnGenreCheck.isEnabled = false
                    btnGenreCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_text_color))
                    btnGenreCheck.setTypeface(null, Typeface.NORMAL)
                    btnGenreCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_color))
                }
            }
            // 필터 다시 선택했을 때 기존 필터 선택데이터가 있다면 버튼 색상 변경
            if(previouslySelectedGenreChips?.isNotEmpty() == true) {
                btnGenreCheck.isEnabled = true
                btnGenreCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                btnGenreCheck.setTypeface(null, Typeface.BOLD)
                btnGenreCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
            } else {
                btnGenreCheck.isEnabled = false
                btnGenreCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_text_color))
                btnGenreCheck.setTypeface(null, Typeface.NORMAL)
                btnGenreCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_color))
            }

            ivGenreFilterClose.setOnClickListener {
                dismiss()
            }

            btnGenreCheck.setOnClickListener {
                val selectedResult = selectedGenreChips?.map { chip -> genreFilterOptions.find { chip == it.first } }
                searchFilterViewModel.getGenreFilteredList(selectedResult)
                searchFilterViewModel.fetchSearchFilterResult()
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "SearchGenreBottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun restorePreviouslySelectedGenreChips() {
        previouslySelectedGenreChips?.forEach { chipId ->
            val chip = binding.cpGroupGenre.findViewById<Chip>(chipId)
            chip.isChecked = true
        }
    }
}