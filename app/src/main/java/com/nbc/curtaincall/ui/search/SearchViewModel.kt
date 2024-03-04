package com.nbc.curtaincall.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient
import com.nbc.curtaincall.search.model.SearchItem
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

    // 로딩
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _failureMessage = MutableLiveData<String>()
    val failureMessage: LiveData<String>
        get() = _failureMessage

    fun fetchSearchResult(search: String) { // 검색어를 통해 api 공연명 요청
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                val result = getSearchResult(search)
                _searchResultList.value = result
            }.onFailure {exception ->
                handleFailure(exception as Exception)
                Log.e("SearchViewModel", "fetchSearchResult: ${exception.message}" )
            }
            _isLoading.value = false
        }
    }

    fun fetchSearchFilterResult() {
        val genreFilteredList = genreFilterResultList.value
        val addrFilteredList = addrFilterResultList.value
        val childFilteredList = childFilterResultList.value

        if(genreFilteredList != null || addrFilteredList != null || childFilteredList != null) {
            val genre: String = genreFilteredList?.mapNotNull { it?.second }?.joinToString (",")  ?: ""
            val addr: String = addrFilteredList?.mapNotNull { it?.second }?.joinToString (",") ?: ""
            val child: String = childFilteredList?.mapNotNull { it?.second }?.joinToString (",")  ?: ""

            Log.d(TAG, "fetchSearchFilterResult genre: $genre")
            Log.d(TAG, "fetchSearchFilterResult addr: $addr")
            Log.d(TAG, "fetchSearchFilterResult child: $child")

            viewModelScope.launch {
                _isLoading.value = true
                runCatching {
                    val result = getSearchResultByFilter(genre, addr, child)
                    _searchResultList.value = result
                }.onFailure { exception ->
                    handleFailure(exception as Exception)
                    Log.e("SearchViewModel", "fetchSearchFilterResult: ${exception.message}")
                }
                _isLoading.value = false
            }
        }
    }

    //검색어 기준
    suspend fun getSearchResult(search: String) = withContext(Dispatchers.IO) {
        RetrofitClient.search.getSearchFilterShowList(shprfnm = search).searchShowList
    }

    // 필터 조건 기준 , null 가능하게 해야
    suspend fun getSearchResultByFilter(genre: String?, addr: String?,  child: String?) = withContext(Dispatchers.IO) {
        val addrInt = addr?.toIntOrNull()
        RetrofitClient.search.getSearchFilterShowList(shcate = genre, signgucode = addrInt, kidstate = child).searchShowList
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

    private fun handleFailure(exception: Exception) {
        _failureMessage.value = "서버 오류가 발생하였습니다"
    }
    companion object{
        const val TAG = "viewmodel"
    }
}