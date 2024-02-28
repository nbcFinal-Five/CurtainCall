package com.nbc.curtaincall.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.data.model.Boxof
import com.nbc.curtaincall.data.model.Db
import com.nbc.curtaincall.data.model.KopisApiInterface
import kotlinx.coroutines.launch

class HomeViewModel(private val kopisApi: KopisApiInterface) : ViewModel() {
    private val _showList = MutableLiveData<List<Db>>()
    val showList: LiveData<List<Db>> get() = _showList

    private val _topRank = MutableLiveData<List<Boxof>>()
    val topRank: LiveData<List<Boxof>> get() = _topRank

    fun fetchUpcomingList() {
        viewModelScope.launch {
            _showList.value = kopisApi.fetchShowList().showList
        }
    }
    fun fetchTopRank(){
        viewModelScope.launch {
            _topRank.value = kopisApi.fetchTopRank().boxof
        }
    }
}