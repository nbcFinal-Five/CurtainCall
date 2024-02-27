package com.nbc.curtaincall.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.data.api.SearchRetrotifClient
import com.nbc.curtaincall.data.model.ShowItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    private val _searchResultList = MutableLiveData<List<ShowItem>>()
    val searchResultList get() = _searchResultList

    fun fetchSearchResult(search: String) {
        viewModelScope.launch {
            runCatching {
                val result = getSearchResult(search)
                _searchResultList.value = result
            }.onFailure {
                Log.e("SearchViewModel", "fetchSearchResult: ${it.message}" )
            }
        }
    }

    suspend fun getSearchResult(search: String) = withContext(Dispatchers.IO) {
        SearchRetrotifClient.kopisApi.getSearchFilterShowList(shprfnm = search).showList
    }
}