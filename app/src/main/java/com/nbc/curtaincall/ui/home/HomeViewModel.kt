package com.nbc.curtaincall.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.data.model.KopisApiInterface
import com.nbc.curtaincall.data.model.ShowItem
import kotlinx.coroutines.launch

class HomeViewModel(private val kopisApi: KopisApiInterface) : ViewModel() {
    private val _showList = MutableLiveData<List<ShowItem>>()
    val showList: LiveData<List<ShowItem>> get() = _showList

    fun fetchUpcomingList() {
        viewModelScope.launch {
            _showList.value = kopisApi.fetchShowList().showList
        }
    }
}