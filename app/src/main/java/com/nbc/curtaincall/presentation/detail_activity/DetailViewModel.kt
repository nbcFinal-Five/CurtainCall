package com.nbc.curtaincall.ui.detail_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.fetch.model.DbResponse
import com.nbc.curtaincall.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val fetchRemoteRepository: FetchRepositoryImpl = FetchRepositoryImpl(fetch = fetch)
    private lateinit var showId: String //공연 id
    private lateinit var facilityId: String //공연장 id
    private val _detailInfoList = MutableLiveData<List<DbResponse>?>()
    val detailInfoList: LiveData<List<DbResponse>?> get() = _detailInfoList

    private var _totalExpectationCount: MutableLiveData<Int> = MutableLiveData(0)
    val totalExpectationCount: LiveData<Int>
        get() = _totalExpectationCount

    private var _point: MutableLiveData<Double> = MutableLiveData(0.0)
    val point: LiveData<Double>
        get() = _point

    private var _isBookmark: MutableLiveData<Boolean> = MutableLiveData(false)
    val isBookmark: LiveData<Boolean>
        get() = _isBookmark

    private val _locationList = MutableLiveData<List<DbResponse>>()
    val locationList: LiveData<List<DbResponse>>
        get() = _locationList

    fun sharedId(mt20Id: String, mt10Id: String) {
        showId = mt20Id
        facilityId = mt10Id
    }

    fun fetchDetailInfo() {
        viewModelScope.launch {
            runCatching {
                _detailInfoList.value =
                    fetchRemoteRepository.fetchShowDetail(path = showId).showList
            }
        }
    }

    fun fetchDetailLocation() {
        viewModelScope.launch {
            runCatching {
                _locationList.value = fetch.getLocationList(path = facilityId).showList!!
                Log.d("ViewModel", "fetchDetailLocation: ${locationList}")
            }.onFailure {
                Log.e("DetailViewModel", "fetchDetailLocation: ${it.message}")
            }
        }
    }
}
