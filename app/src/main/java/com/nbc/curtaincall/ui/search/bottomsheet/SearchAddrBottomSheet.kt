package com.nbc.curtaincall.ui.search.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nbc.curtaincall.databinding.SearchBottomsheetDialogAddrBinding
import com.nbc.curtaincall.ui.search.SearchListAdapter
import com.nbc.curtaincall.ui.search.SearchViewModel

class SearchAddrBottomSheet : BottomSheetDialogFragment() {

    private var _binding: SearchBottomsheetDialogAddrBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchViewModel  by activityViewModels<SearchViewModel>()
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

        with(binding) {
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