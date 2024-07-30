package com.nbc.curtaincall.ui.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import com.nbc.curtaincall.di.RetrofitClientModule
import com.nbc.curtaincall.search.model.SearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    // 검색 결과 리스트
    private val _searchResultList = MutableLiveData<List<SearchItem>?>()
    val searchResultList : LiveData<List<SearchItem>?>
        get() = _searchResultList

    // 지역 선택한 필터 리스트
    private val _saveCategoryAddrTitle = MutableLiveData<List<Int>>()
    val saveCategoryAddrTitle: LiveData<List<Int>>
        get() = _saveCategoryAddrTitle

    // 장르 선택한 필터 리스트
    private val _saveCategoryGenreTitle = MutableLiveData<List<Int>>()
    val saveCategoryGenreTitle: LiveData<List<Int>>
        get() = _saveCategoryGenreTitle

    private val _saveCategoryChildTitle = MutableLiveData<List<Int>>()
    val saveCategoryChildTitle: LiveData<List<Int>>
        get() = _saveCategoryChildTitle

    // 필터에서 요청한 값
   private val _addrFilterResultList = MutableLiveData<List<Pair<Chip,String>?>?>()
    val addrFilterResultList : LiveData<List<Pair<Chip,String>?>?>
        get() = _addrFilterResultList

    private val _childFilterResultList = MutableLiveData<List<Pair<Chip,String>?>?>()
    val childFilterResultList : LiveData<List<Pair<Chip,String>?>?>
        get() = _childFilterResultList

    private val _genreFilterResultList = MutableLiveData<List<Pair<Chip,String>?>?>()
    val genreFilterResultList : LiveData<List<Pair<Chip,String>?>?>
        get() = _genreFilterResultList

    // 검색 키워드
    private val _searchWord = MutableLiveData<String?>()
    val searchWord : LiveData<String?> get() = _searchWord

    // 로딩
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    // 무한 스크롤 로딩
    private val _isNextLoading = MutableLiveData<Boolean>(false)
    val isNextLoading: LiveData<Boolean>
        get() = _isNextLoading

    private val _nextResultState = MutableLiveData<Boolean>()
    val nextResultState: LiveData<Boolean>
        get() = _nextResultState

    private val _failureMessage = MutableLiveData<String>()
    val failureMessage: LiveData<String>
        get() = _failureMessage

    private var nextPage = 2
    private var canLoadMore = true

    @SuppressLint("SuspiciousIndentation")
    fun fetchSearchFilterResult() {
        val genreFilteredList = genreFilterResultList.value
        val addrFilteredList = addrFilterResultList.value
        val childFilteredList = childFilterResultList.value

        val genre: String = genreFilteredList?.mapNotNull { it?.second }?.joinToString ("|")  ?: ""
        val addr: String = addrFilteredList?.mapNotNull { it?.second }?.joinToString ("|") ?: ""
        val child: String = childFilteredList?.mapNotNull { it?.second }?.joinToString ("|")  ?: ""
        val searchWord = searchWord.value

            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    nextPage = 2
                    canLoadMore = true
                    val result = getSearchResultByFilter(searchWord, genre, addr, child)
                    _searchResultList.postValue(result)
                } catch (e : Exception){
                    handleFailure(e)
                    Log.e(TAG, "fetchSearchFilterResult: ${e.message}")
                } finally {
                    _isLoading.value = false
                }
            }
    }

    // 필터 조건 기준 , null 가능하게 해야
    suspend fun getSearchResultByFilter(search: String?, genre: String?, addr: String?,  child: String?) = withContext(Dispatchers.IO) {
        RetrofitClientModule.search.getSearchFilterShowList(cpage = 1, shprfnm = search, shcate = genre, signgucode = addr, kidstate = child).searchShowList
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
            if(canLoadMore) {
                try {
                    _isNextLoading.value = true
                    val nextResult = getSearchResultNextPage(nextPage, searchWord, genre, addr, child)
                    val currentList = _searchResultList.value.orEmpty().toMutableList()

                    if(nextResult == null) {
                        _nextResultState.postValue(false)
                    } else {
                        nextResult.let { currentList.addAll(it) }
                        _searchResultList.postValue(currentList)
                        nextPage++
                        _nextResultState.postValue(true)
                    }
                } catch (e : Exception) {
                    handleFailure(e)
                    Log.e(TAG, "loadMoreSearchResult: ${e.message}")
                } finally {
                    _isNextLoading.value = false
                }
            }
        }
    }

    private suspend fun getSearchResultNextPage(nextPage: Int, search: String?, genre: String?, addr: String?,  child: String?) = withContext(Dispatchers.IO) {
        RetrofitClientModule.search.getSearchFilterShowList(cpage = nextPage, shprfnm = search, shcate = genre, signgucode = addr, kidstate = child).searchShowList
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

    fun resetData() {
        _genreFilterResultList.value = null
        _addrFilterResultList.value = null
        _childFilterResultList.value = null
        _searchWord.value = null
        _saveCategoryAddrTitle.value = listOf()
        _saveCategoryGenreTitle.value = listOf()
        _saveCategoryChildTitle.value = listOf()
        _searchResultList.value = null
        _nextResultState.postValue(true)
    }

    //선택된 칩 위치를 기억 하기 위한 코드
    fun saveCategoryAddrTitle(category: List<Int>) {
        _saveCategoryAddrTitle.value = category
    }

    fun saveCategoryGenreTitle(category: List<Int>) {
        _saveCategoryGenreTitle.value = category
    }

    fun saveCategoryChildTitle(category: List<Int>) {
        _saveCategoryChildTitle.value = category
    }

    fun setCanLoadMore (value : Boolean) {
        canLoadMore = value
    }

    fun setNextResultState (value: Boolean) {
        _nextResultState.postValue(value)
    }

    private fun handleFailure(exception: Exception) {
        _failureMessage.value = "서버에서 응답을 받을 수 없습니다. \n나중에 다시 시도해주세요"
    }

    companion object{
        const val TAG = "SearchViewModel"
    }
}