package com.nbc.curtaincall.ui.search.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.databinding.SearchBottomsheetDialogChildrenBinding
import com.nbc.curtaincall.ui.search.SearchFilterViewModel
import com.nbc.curtaincall.ui.search.SearchListAdapter
import com.nbc.curtaincall.ui.search.SearchViewModel

class SearchChildrenBottomSheet : BottomSheetDialogFragment() {

    private var _binding: SearchBottomsheetDialogChildrenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchFilterViewModel  by activityViewModels<SearchFilterViewModel>()
    private val searchListAdapter by lazy { SearchListAdapter() }
    private val childrenFilterOptions by lazy {
        listOf(
            with(binding){
                cpChildrenPossible to "Y"
                cpChildrenImpossible to "N"
            }
        )
    }
    private val selectedChips: MutableList<String> = mutableListOf()
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
            ivChildrenFilterClose.setOnClickListener {
                dismiss()
            }

            btnChildrenCheck.setOnClickListener {
            cpgroupChildrenChoice.setOnCheckedStateChangeListener { group, checkedIds ->
                // 뷰모델이나? 칩이 선택되었을 때 담길 리스트 변수 필요      selectedOptions[index].clear()
//                for(id in childrenFilterOptions.indices) {
//                    if(checkedIds == cpgroupChildrenChoice.get(id).first)
//                    val selectedOption = childrenFilterOptions[index].first{it.first == id}

                cpChildrenPossible?.let {
                    val chipText = cpChildrenPossible.text.toString()
                    if(cpChildrenPossible.isChecked) {
                        if(!selectedChips.contains(chipText)) {
                            selectedChips.add(chipText)
                        } else {
                            selectedChips.remove(chipText)
                        }
                }

                }
            }
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