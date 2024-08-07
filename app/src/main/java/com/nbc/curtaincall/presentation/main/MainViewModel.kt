package com.nbc.curtaincall.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.usecase.GetShowDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getShowDetail: GetShowDetail) : ViewModel() {
    //공연 상세 정보 리스트
    private val _showDetailInfo = MutableLiveData<DbsEntity<DbsShowListEntity>>()
    val showDetailInfo: LiveData<DbsEntity<DbsShowListEntity>> get() = _showDetailInfo

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
            _showDetailInfo.value = getShowDetail(path = id)
        }
    }
}