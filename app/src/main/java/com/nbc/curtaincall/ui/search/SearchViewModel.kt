package com.nbc.curtaincall.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.data.api.RetrofitClient
import com.nbc.curtaincall.data.model.ShowList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _searchResultList = MutableLiveData<ShowList>()
    val searchResultList get() = _searchResultList

    fun fetchSearchResult(search: String) {
        viewModelScope.launch {
            runCatching {
                val result = getSearchResult(search)
//                _searchResultList.value = result
            }.onFailure {
                Log.e("SearchViewModel", "fetchSearchResult: ${it.message}" )
            }
        }
    }

    suspend fun getSearchResult(search: String) = withContext(Dispatchers.IO) {
        RetrofitClient.kopisApi.getSearchShowList(shprfnm=search).showList
    }
}