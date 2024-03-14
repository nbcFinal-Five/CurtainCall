package com.nbc.curtaincall.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.fetch.model.DbResponse
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
import kotlinx.coroutines.launch

class MainViewModel(private val fetchRemoteRepository: FetchRepositoryImpl) : ViewModel() {
    //공연 상세 정보 리스트
    private val _showDetailInfo = MutableLiveData<List<DbResponse>?>()
    val showDetailInfo: LiveData<List<DbResponse>?> get() = _showDetailInfo
    //공연 id
    private val _showId = MutableLiveData<String>()
    val showId: LiveData<String> get() = _showId

    //id를 공유 하기 위한 함수
    fun sharedShowId(id: String) {
        _showId.value = id
    }

    //공연 상세 정보를 받아옴
    fun fetchShowDetail(id: String) {
        viewModelScope.launch {
            _showDetailInfo.value = fetchRemoteRepository.fetchShowDetail(path = id).showList
        }
    }
}

class MainViewModelFactory(private val fetchRemoteRepository: FetchRepositoryImpl):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(fetchRemoteRepository) as T
    }
}