package com.nbc.curtaincall.ui.search.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.databinding.SearchBottomsheetDialogGenreBinding
import com.nbc.curtaincall.ui.search.SearchListAdapter
import com.nbc.curtaincall.ui.search.SearchViewModel

class SearchGenreBottomSheet : BottomSheetDialogFragment() {

    private var _binding: SearchBottomsheetDialogGenreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchViewModel  by activityViewModels<SearchViewModel>()
    private val searchListAdapter by lazy { SearchListAdapter() }
    private val genreFilterOptions by lazy {
        listOf(
            listOf(
                with(binding){
                    cpBottomCircus to "EEEB"
                    cpBottomClassic to "CCCA"
                    cpBottomFutility to "BBBC"
                    cpBottomMix to "EEEA"
                    cpBottomMusical to "GGGA"
                    cpBottomKoreanClassic to "CCCC"
                    cpBottomPopularMusic to "CCCD"
                    cpBottomPublicfutility to "BBBE"
                    cpBottomTheater to "AAAA"
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
        _binding = SearchBottomsheetDialogGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGenreCheck.setOnClickListener {
            dismiss()
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