package com.nbc.curtaincall.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.data.model.DbResponse
import com.nbc.curtaincall.domain.repository.FetchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val fetch: FetchRepository) : ViewModel() {
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
            _showDetailInfo.value = fetch.fetchShowDetail(path = id)
        }
    }
}