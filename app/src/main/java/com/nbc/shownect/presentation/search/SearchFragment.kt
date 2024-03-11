package com.nbc.shownect.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentSearchBinding
import com.nbc.shownect.fetch.network.retrofit.RetrofitClient
import com.nbc.shownect.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.shownect.ui.home.adapter.PosterClickListener
import com.nbc.shownect.ui.main.MainViewModel
import com.nbc.shownect.ui.main.MainViewModelFactory
import com.nbc.shownect.ui.search.bottomsheet.SearchAddrBottomSheet
import com.nbc.shownect.ui.search.bottomsheet.SearchChildrenBottomSheet
import com.nbc.shownect.ui.search.bottomsheet.SearchGenreBottomSheet
import com.nbc.shownect.ui.ticket.TicketDialogFragment
import com.nbc.shownect.util.sharedpreferences.App

class SearchFragment : Fragment(), PosterClickListener {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val searchListAdapter by lazy { SearchListAdapter(this) }
    private val searchViewModel  by activityViewModels<SearchViewModel>()
    private val sharedViewModel: MainViewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory(
            fetchRemoteRepository = FetchRepositoryImpl(
                RetrofitClient.fetch
            )
        )
    }

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
        showSearchList()
        changeFilterUiDesign()
        filterReset()
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

            tvSearchfilterChild.setOnClickListener {
                val chilrenBottomSheet = SearchChildrenBottomSheet()
                chilrenBottomSheet.show(childFragmentManager, chilrenBottomSheet.tag)
                chilrenBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            }
        }
    }

    private fun showSearchList() { // 검색어 입력시 결과 확인
        with(binding) {
            etSearch.setOnKeyListener { v, keyCode, event ->
                when(keyCode){
                    KeyEvent.KEYCODE_ENTER -> ivSearch.callOnClick()
                    else -> ivSearch.canScrollVertically(0)
                }
            }

            ivSearch.setOnClickListener {// 제목 기준으로 검색하기
                hideKeyboard()
                App.prefs.saveSearchWord(etSearch.text?.toString()?.trim() ?:"")
                searchViewModel.getSearchWord(etSearch.text?.toString()?.trim() ?:"")
                searchViewModel.fetchSearchFilterResult()
            }

            // 검색 시 스켈레톤 보여주기
            searchViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    clSearchSkeleton.visibility = View.VISIBLE
                    tvSearchNoresult.visibility = View.GONE
                    ivSearchNoresult.visibility = View.GONE
                    rvSearch.visibility = View.INVISIBLE
                } else {
                    clSearchSkeleton.visibility = View.GONE
                    rvSearch.visibility = View.VISIBLE
                }
            }

            // 통신 장애시 안내 문구
            searchViewModel.failureMessage.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
                }

            }

            // 검색 리스트 구독하여 변경점 생기면, listadapter가 확인하여 리사이클러뷰 업데이트 , 검색결과 안내 text visible 유무
            searchViewModel.searchResultList.observe(viewLifecycleOwner) { result ->
                searchListAdapter.submitList(result)
                if(result == null) {
                    tvSearchNoresult.visibility = View.VISIBLE
                    ivSearchNoresult.visibility = View.VISIBLE
                } else {
                    tvSearchNoresult.visibility = View.GONE
                    ivSearchNoresult.visibility = View.GONE
                }
            }

        }
    }

@SuppressLint("SetTextI18n")
private fun changeFilterUiDesign() {
    searchViewModel.genreFilterResultList.observe(viewLifecycleOwner) { genreList ->
        if (genreList != null && genreList.isNotEmpty()) {
            val selectedGenreCount = genreList.size
            with(binding) {
                tvSearchfilterGenre.setText(getString(R.string.filter_genre) + " : ${selectedGenreCount}")
                tvSearchfilterGenre.setBackgroundResource(R.drawable.shape_searchfilter_selected_radius)
                tvSearchfilterGenre.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                tvSearchfilterGenre.setTypeface(null, Typeface.BOLD)
                ivGenreArrow.visibility = View.GONE
            }
        } else {
            with(binding) {
                tvSearchfilterGenre.setText(R.string.filter_genre)
                tvSearchfilterGenre.setBackgroundResource(R.drawable.shape_searchfilter_radius)
                tvSearchfilterGenre.setTypeface(null, Typeface.NORMAL)
                ivGenreArrow.visibility = View.VISIBLE
            }
        }
    }

    searchViewModel.addrFilterResultList.observe(viewLifecycleOwner) { addrList ->
        if (addrList != null && addrList.isNotEmpty()) {
            val selectedAddrCount = addrList.size
            with(binding) {
                tvSearchfilterAddr.setText(getString(R.string.filter_addr) + " : ${selectedAddrCount}")
                tvSearchfilterAddr.setBackgroundResource(R.drawable.shape_searchfilter_selected_radius)
                tvSearchfilterAddr.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                tvSearchfilterAddr.setTypeface(null, Typeface.BOLD)
                ivAddrArrow.visibility = View.GONE
            }
        } else {
            with(binding) {
                tvSearchfilterAddr.setText(R.string.filter_addr)
                tvSearchfilterAddr.setBackgroundResource(R.drawable.shape_searchfilter_radius)
                tvSearchfilterAddr.setTypeface(null, Typeface.NORMAL)
                ivAddrArrow.visibility = View.VISIBLE
            }
        }
    }

    searchViewModel.childFilterResultList.observe(viewLifecycleOwner) { childList ->
        if (childList != null && childList.isNotEmpty()) {
            val selectedChildCount = childList.size
            with(binding) {
                tvSearchfilterChild.setText(getString(R.string.filter_children) + " : ${selectedChildCount}")
                tvSearchfilterChild.setBackgroundResource(R.drawable.shape_searchfilter_selected_radius)
                tvSearchfilterChild.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                tvSearchfilterChild.setTypeface(null, Typeface.BOLD)
                ivAddrArrow.visibility = View.GONE
            }
        } else {
            with(binding) {
                tvSearchfilterChild.setText(R.string.filter_children)
                tvSearchfilterChild.setBackgroundResource(R.drawable.shape_searchfilter_radius)
                tvSearchfilterChild.setTypeface(null, Typeface.NORMAL)
                ivAddrArrow.visibility = View.VISIBLE
            }
        }
    }
}

    private fun initList() { // 검색 결과 recyclerview 만들기
        with(binding) {
            with(rvSearch) {
                adapter = searchListAdapter
                layoutManager = GridLayoutManager(requireActivity(),3)
                setHasFixedSize(true)

                // 무한 스크롤을 위한 리사이클러뷰 위치 감지
//                addOnScrollListener(object: RecyclerView.OnScrollListener(){
//                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                        super.onScrolled(recyclerView, dx, dy)
//                        val visibleThreshold = 1
//                        val layoutManager = layoutManager as GridLayoutManager
//                        val visibleItemCount = layoutManager.childCount
//                        val totalItemCount = layoutManager.itemCount
//                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//                        val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
//                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
//
//                        // 리스트의 마지막에 도달할 때 추가 검색 데이터 요청
//
//                            if (totalItemCount - lastVisibleItemPosition <= visibleThreshold
//                            ) {
//                                searchViewModel.loadMoreSearchResult()
//                            }
//                    }
//                })
            }
        }
    }

    private fun filterReset(){
        binding.ivSearchFilterReset.setOnClickListener {
            searchViewModel.resetData()
        }
    }

    override fun posterClicked(id: String) { // 해당 아이템 클릭시 간단화면 띄우기
        val ticketDialog = TicketDialogFragment()
        sharedViewModel.sharedShowId(id) //해당 공연의 id를 MainViewModel로 보내줌
        ticketDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
        ticketDialog.show(childFragmentManager, ticketDialog.tag)
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