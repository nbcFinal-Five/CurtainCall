package com.nbc.curtaincall.ui.search.bottomsheet

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.SearchBottomsheetDialogChildrenBinding
import com.nbc.curtaincall.ui.search.SearchListAdapter
import com.nbc.curtaincall.ui.search.SearchViewModel

class SearchChildrenBottomSheet : BottomSheetDialogFragment() {

    private var _binding: SearchBottomsheetDialogChildrenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchFilterViewModel  by activityViewModels<SearchViewModel>()
    private val childrenFilterOptions by lazy {
        with(binding){
            listOf(
                cpChildrenPossible to "Y",
                cpChildrenImpossible to "N"
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
        _binding = SearchBottomsheetDialogChildrenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickFilterButton()
    }

    private fun clickFilterButton() {
        with(binding) {
            cpGroupChildren.setOnCheckedStateChangeListener { group, checkedIds ->
                if(checkedIds.size>0 ) {
                    btnChildrenCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                    btnChildrenCheck.setTypeface(null, Typeface.BOLD)
                    btnChildrenCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
                } else {
                    btnChildrenCheck.setTextColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_text_color))
                    btnChildrenCheck.setTypeface(null, Typeface.NORMAL)
                    btnChildrenCheck.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.filter_btn_color))
                }
            }

            ivChildrenFilterClose.setOnClickListener {
                dismiss()
            }

            btnChildrenCheck.setOnClickListener {
                val selectedResult = selectedChips?.map { chip ->  childrenFilterOptions.find { chip == it.first }}
                searchFilterViewModel.getChildFilteredList(selectedResult)
//                searchFilterViewModel.fetchSearchFilterResult()
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "SearchChildrenBottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}