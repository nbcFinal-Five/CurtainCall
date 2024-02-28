package com.nbc.curtaincall.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient
import com.nbc.curtaincall.search.model.SearchItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    private val _searchResultList = MutableLiveData<List<SearchItem>?>()
    val searchResultList :LiveData<List<SearchItem>?>
        get() = _searchResultList

   private val _filterResultList = MutableLiveData<List<SearchItem>?>()
    val filterResultList : LiveData<List<SearchItem>?>
        get() = _filterResultList

    // 로딩
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _failureMessage = MutableLiveData<String>()
    val failureMessage: LiveData<String>
        get() = _failureMessage

    fun fetchSearchResult(search: String) {
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

    suspend fun getSearchResult(search: String) = withContext(Dispatchers.IO) {
        RetrofitClient.search.getSearchFilterShowList(shprfnm = search).searchShowList
    }

    private fun handleFailure(exception: Exception) {
        _failureMessage.value = "서버 오류가 발생하였습니다."
    }
}