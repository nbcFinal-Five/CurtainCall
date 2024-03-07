package com.nbc.shownect.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import com.nbc.shownect.fetch.network.retrofit.RetrofitClient
import com.nbc.shownect.search.model.SearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    private val _searchResultList = MutableLiveData<List<SearchItem>?>()
    val searchResultList :LiveData<List<SearchItem>?>
        get() = _searchResultList

   private val _addrFilterResultList = MutableLiveData<List<Pair<Chip,String>?>?>()
    val addrFilterResultList : LiveData<List<Pair<Chip,String>?>?>
        get() = _addrFilterResultList

    private val _childFilterResultList = MutableLiveData<List<Pair<Chip,String>?>?>()
    val childFilterResultList : LiveData<List<Pair<Chip,String>?>?>
        get() = _childFilterResultList

    private val _genreFilterResultList = MutableLiveData<List<Pair<Chip,String>?>?>()
    val genreFilterResultList : LiveData<List<Pair<Chip,String>?>?>
        get() = _genreFilterResultList

    private val _searchWord = MutableLiveData<String?>()
    val searchWord : LiveData<String?> get() = _searchWord

    // 로딩
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _failureMessage = MutableLiveData<String>()
    val failureMessage: LiveData<String>
        get() = _failureMessage

//    private val _currentPage = MutableLiveData<Int>(1)
//    val currentPage:LiveData<Int>
//        get() = _currentPage

    private var currentPage = 1



    fun fetchSearchFilterResult() {
        val genreFilteredList = genreFilterResultList.value
        val addrFilteredList = addrFilterResultList.value
        val childFilteredList = childFilterResultList.value

        val genre: String = genreFilteredList?.mapNotNull { it?.second }?.joinToString ("|")  ?: ""
        val addr: String = addrFilteredList?.mapNotNull { it?.second }?.joinToString ("|") ?: ""
        val child: String = childFilteredList?.mapNotNull { it?.second }?.joinToString ("|")  ?: ""
        val searchWord = searchWord.value

            viewModelScope.launch {
                _isLoading.value = true
                runCatching {
                    val result = getSearchResultByFilter(searchWord, genre, addr, child)
                    _searchResultList.value = result
                }.onFailure { exception ->
                    handleFailure(exception as Exception)
                    Log.e("SearchViewModel", "fetchSearchFilterResult: ${exception.message}")
                }
                _isLoading.value = false
            }
    }

    // 필터 조건 기준 , null 가능하게 해야
    suspend fun getSearchResultByFilter(search: String?, genre: String?, addr: String?,  child: String?) = withContext(Dispatchers.IO) {
        RetrofitClient.search.getSearchFilterShowList(cpage = "1", shprfnm = search, shcate = genre, signgucode = addr, kidstate = child).searchShowList
    }

    fun loadMoreSearchResult() {
        val genreFilteredList = genreFilterResultList.value
        val addrFilteredList = addrFilterResultList.value
        val childFilteredList = childFilterResultList.value

        val genre: String = genreFilteredList?.mapNotNull { it?.second }?.joinToString ("|")  ?: ""
        val addr: String = addrFilteredList?.mapNotNull { it?.second }?.joinToString ("|") ?: ""
        val child: String = childFilteredList?.mapNotNull { it?.second }?.joinToString ("|")  ?: ""
        val searchWord = searchWord.value

        viewModelScope.launch {
            runCatching {

            }.onFailure {
                Log.e("SearchViewModel", "loadMoreSearchResult: ${it.message}", )

            }
        }
    }

    private suspend fun getSearchResultNextPage(nextPage: Int,search: String?, genre: String?, addr: String?,  child: String?) = withContext(Dispatchers.IO) {
        RetrofitClient.search.getSearchFilterShowList(cpage = nextPage.toString(), shprfnm = search, shcate = genre, signgucode = addr, kidstate = child).searchShowList
    }

    // 필터 창에서 선택한 값들을 filterResultList에 담기
    fun getGenreFilteredList(selectedChipList : List<Pair<Chip,String>?>?) {
        _genreFilterResultList.value = selectedChipList
    }

    fun getAddrFilteredList(selectedChipList : List<Pair<Chip,String>?>?) {
        _addrFilterResultList.value = selectedChipList
    }

    fun getChildFilteredList(selectedChipList : List<Pair<Chip,String>?>?) {
        _childFilterResultList.value = selectedChipList
    }

    fun getSearchWord(search: String) {
        _searchWord.value = search
    }

    private fun handleFailure(exception: Exception) {
        _failureMessage.value = "서버 오류가 발생하였습니다"
    }
    companion object{
        const val TAG = "viewmodel"
    }
}