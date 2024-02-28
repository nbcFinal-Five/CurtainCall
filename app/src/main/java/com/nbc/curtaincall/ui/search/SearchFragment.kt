package com.nbc.curtaincall.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentSearchBinding
import com.nbc.curtaincall.ui.search.bottomsheet.SearchAddrBottomSheet
import com.nbc.curtaincall.ui.search.bottomsheet.SearchChildrenBottomSheet
import com.nbc.curtaincall.ui.search.bottomsheet.SearchGenreBottomSheet

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchListAdapter by lazy { SearchListAdapter() }
    private val searchViewModel  by activityViewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initList()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomSheet()
        searchShowList()
    }

    private fun showBottomSheet() { // 검색 필터 클릭시 bottom sheet 띄우기
        with(binding) {
            tvSearchfilterGenre.setOnClickListener {
                val genreBottomSheet = SearchGenreBottomSheet()
                genreBottomSheet.show(childFragmentManager, genreBottomSheet.tag)
                genreBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            }

            tvSearchfilterAddr.setOnClickListener {
                val addrBottomSheet = SearchAddrBottomSheet()
                addrBottomSheet.show(childFragmentManager, addrBottomSheet.tag)
                addrBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            }

            tvSearchfilterChildren.setOnClickListener {
                val chilrenBottomSheet = SearchChildrenBottomSheet()
                chilrenBottomSheet.show(childFragmentManager, chilrenBottomSheet.tag)
                chilrenBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            }
        }
    }

    private fun searchShowList() { // 검색어 입력시 결과 확인
        with(binding) {
            etSearch.setOnKeyListener { v, keyCode, event ->
                when(keyCode){
                    KeyEvent.KEYCODE_ENTER -> ivSearch.callOnClick()
                    else -> ivSearch.canScrollVertically(0)
                }
            }

            ivSearch.setOnClickListener {
                hideKeyboard()
                searchViewModel.fetchSearchResult(etSearch.text?.toString()?.trim() ?:"")
                searchViewModel.searchResultList.observe(viewLifecycleOwner) {
                    searchListAdapter.submitList(it)
                    if(it == null) {
                        tvSearchNoresult.visibility = View.VISIBLE
                    } else {
                        tvSearchNoresult.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initList() { // 검색 결과 recyclerview 만들기
        with(binding) {
            with(rvSearch) {
                adapter = searchListAdapter.apply {
                    itemClick = object : SearchListAdapter.ItemCLick{
                        override fun onClick(position: Int) {
                        }
                    }
                }
                layoutManager = GridLayoutManager(requireActivity(),3)
                setHasFixedSize(true)
            }
        }
    }

    private fun hideKeyboard() { // 키보드 숨기기
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}