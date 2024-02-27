package com.nbc.curtaincall.ui.search.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.databinding.SearchBottomsheetDialogChildrenBinding
import com.nbc.curtaincall.ui.search.SearchListAdapter
import com.nbc.curtaincall.ui.search.SearchViewModel

class SearchChildrenBottomSheet : BottomSheetDialogFragment() {

    private var _binding: SearchBottomsheetDialogChildrenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchViewModel  by activityViewModels<SearchViewModel>()
    private val searchListAdapter by lazy { SearchListAdapter() }
    private val childrenFilterOptions by lazy {
        listOf(
            listOf(
                with(binding){
                    cpBottomChildrenPossible to "Y"
                    cpBottomChildrenImpossible to "N"
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
        _binding = SearchBottomsheetDialogChildrenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChildrenCheck.setOnClickListener {
            dismiss()
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