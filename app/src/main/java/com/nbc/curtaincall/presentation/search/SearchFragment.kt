package com.nbc.curtaincall.ui.search

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
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentSearchBinding
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.curtaincall.ui.home.adapter.PosterClickListener
import com.nbc.curtaincall.ui.main.MainViewModel
import com.nbc.curtaincall.ui.main.MainViewModelFactory
import com.nbc.curtaincall.ui.search.bottomsheet.SearchAddrBottomSheet
import com.nbc.curtaincall.ui.search.bottomsheet.SearchChildrenBottomSheet
import com.nbc.curtaincall.ui.search.bottomsheet.SearchGenreBottomSheet
import com.nbc.curtaincall.ui.ticket.TicketDialogFragment
import com.nbc.curtaincall.util.sharedpreferences.App

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

            // 서버 통신 문제시
            searchViewModel.failureMessage.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
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
                addOnScrollListener(object: RecyclerView.OnScrollListener(){
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)

                        val visibleThreshold = 1
                        val layoutManager = layoutManager as GridLayoutManager
                        val totalItemCount = layoutManager.itemCount // rv의 총 항목의 갯수
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition() // RecyclerView에서 마지막으로 보이는 항목의 인덱스

                        // 리스트의 마지막에 도달할 때 추가 검색 데이터 요청
                        if (searchViewModel.searchResultList.value != null && !searchViewModel.isNextLoading.value!! &&
                            totalItemCount <= lastVisibleItemPosition + visibleThreshold
                        ) {
                            searchViewModel.loadMoreSearchResult()
                        }
                    }
                })

                // 더이상 데이터를 받아 올 수 없을 때 안내창
                searchViewModel.nextResultState.observe(viewLifecycleOwner) {hasData ->
                    if(!hasData) {
                        Toast.makeText(requireContext(), getString(R.string.search_nextresult_not), Toast.LENGTH_SHORT).show()
                        searchViewModel.setCanLoadMore(false)
                    }
                }

                // 무한스크롤로 인한 추가 데이터 요청시 로딩바 제공
                searchViewModel.isNextLoading.observe(viewLifecycleOwner) {isloading ->
                    if(isloading) {
                        pbNextresultLoading.visibility = View.VISIBLE
                    } else {
                        pbNextresultLoading.visibility = View.GONE
                    }
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

    private fun filterReset(){
        with(binding) {
            llSearchFilterReset.setOnClickListener {
                searchViewModel.resetData()
                searchListAdapter.submitList(null)
                Toast.makeText(requireActivity(),R.string.search_result_reset,Toast.LENGTH_SHORT).show()
                tvSearchNoresult.visibility = View.VISIBLE
                ivSearchNoresult.visibility = View.VISIBLE
            }
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

    override fun onPause() {
        super.onPause()
        // 무한 스크롤 추가 검색 결과 없을 시 토스트 메세지 띄우는것 1회만 하도록
        searchViewModel.setNextResultState(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}