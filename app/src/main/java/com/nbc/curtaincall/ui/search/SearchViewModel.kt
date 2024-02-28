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
    val searchResultList :LiveData<List<SearchItem>?> get() = _searchResultList

   private val _filterResultList = MutableLiveData<List<SearchItem>?>()
    val filterResultList : LiveData<List<SearchItem>?> get() = _filterResultList


    fun fetchSearchChildrenFilterResult(childChoice: String) {
        viewModelScope.launch {
            runCatching {
                val result = getSearchChildrenFilterResult(childChoice)
                _filterResultList.value = result
            }.onFailure {
                Log.e("SearchViewModel", "fetchSearchChildrenFilterResult: ${it.message}" )
            }
        }
    }

    suspend fun getSearchChildrenFilterResult(childChoice: String) = withContext(Dispatchers.IO) {
        RetrofitClient.search.getSearchFilterShowList(kidstate = childChoice).searchShowList
    }

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
        RetrofitClient.search.getSearchFilterShowList(shprfnm = search).searchShowList
    }
}