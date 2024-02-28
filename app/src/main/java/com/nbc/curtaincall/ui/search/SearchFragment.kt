package com.nbc.curtaincall.ui.search

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import com.nbc.curtaincall.util.sharedpreferences.App

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
                App.prefs.saveSearchWord(etSearch.text?.toString()?.trim() ?:"")
                searchViewModel.fetchSearchResult(etSearch.text?.toString()?.trim() ?:"")

                // 검색 시 로딩바 보여주기
                searchViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                    if (isLoading) {
                        pbSearchLoading.visibility = View.VISIBLE
                        tvSearchNoresult.visibility = View.GONE
                        rvSearch.visibility = View.GONE
                    } else {
                        pbSearchLoading.visibility = View.GONE
                        rvSearch.visibility = View.VISIBLE
                    }
                }

                // 통신 장애시 안내 문구
                searchViewModel.failureMessage.observe(viewLifecycleOwner) {
                    if (!it.isNullOrEmpty()) {
                        Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
                    }

                }

                // 검색결과 안내 text
                searchViewModel.searchResultList.observe(viewLifecycleOwner) { result ->
                    searchListAdapter.submitList(result)
                    if(result == null) {
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

    override fun onResume() {
        super.onResume()
        // 검색했던 검색어 검색창에 불러오기
        binding.etSearch.setText(App.prefs.loadSearchWord())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}