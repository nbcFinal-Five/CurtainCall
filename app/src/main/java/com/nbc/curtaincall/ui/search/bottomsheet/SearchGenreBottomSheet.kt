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
import com.nbc.curtaincall.ui.search.SearchListAdapter
import com.nbc.curtaincall.ui.search.SearchViewModel

class SearchGenreBottomSheet : BottomSheetDialogFragment() {

    private var _binding: SearchBottomsheetDialogGenreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchFilterViewModel  by activityViewModels<SearchViewModel>()
    private val searchListAdapter by lazy { SearchListAdapter() }
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
                cpBottomTheater to "AAAA",
            )
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = SearchBottomsheetDialogGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       clickFilterButton()

    }

    private fun clickFilterButton() {
        with(binding) {
            cpGroupGenre.setOnCheckedStateChangeListener { group, checkedIds ->
                if(checkedIds.size > 0) {
                    btnGenreCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                    btnGenreCheck.setTypeface(null, Typeface.BOLD)
                    btnGenreCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
                } else {
                    btnGenreCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_text_color))
                    btnGenreCheck.setTypeface(null, Typeface.NORMAL)
                    btnGenreCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_color))
                }
            }


            ivGenreFilterClose.setOnClickListener {
                dismiss()
            }

            btnGenreCheck.setOnClickListener {
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
}