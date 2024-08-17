package com.nbc.curtaincall.ui.detail_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.repository.FetchRepository
import com.nbc.curtaincall.presentation.model.ShowItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val fetch: FetchRepository) : ViewModel() {
    private lateinit var showId: String //공연 id
    private lateinit var facilityId: String //공연장 id
    private val _detailInfoList = MutableLiveData<List<ShowItem.DetailShowItem>>()
    val detailInfoList: LiveData<List<ShowItem.DetailShowItem>> get() = _detailInfoList

    private var _totalExpectationCount: MutableLiveData<Int> = MutableLiveData(0)
    val totalExpectationCount: LiveData<Int>
        get() = _totalExpectationCount

    private var _point: MutableLiveData<Double> = MutableLiveData(0.0)
    val point: LiveData<Double>
        get() = _point

    private var _isBookmark: MutableLiveData<Boolean> = MutableLiveData(false)
    val isBookmark: LiveData<Boolean>
        get() = _isBookmark

    private val _locationList = MutableLiveData<List<ShowItem.LocationItem>>()
    val locationList: LiveData<List<ShowItem.LocationItem>>
        get() = _locationList

    fun sharedId(mt20Id: String, mt10Id: String) {
        showId = mt20Id
        facilityId = mt10Id
    }

    fun fetchDetailInfo() {
        viewModelScope.launch {
            runCatching {
                createItem(fetch.fetchShowDetail(path = showId))
            }.onSuccess { result ->
                _detailInfoList.value = result
            }.onFailure { _detailInfoList.value = emptyList() }
        }
    }

    private fun createItem(showItem: DbsEntity<DbsShowListEntity>): List<ShowItem.DetailShowItem> =
        showItem.showList?.map { items ->
            ShowItem.DetailShowItem(
                showId = items.showId,
                facilityId = items.prfFacility,
                title = items.performanceName,
                placeName = items.facilityName,
                genre = items.genreName,
                posterPath = items.posterPath,
                age = items.prfAge,
                productCast = items.entrpsnm,
                periodTo = items.prfpdto,
                periodFrom = items.prfpdfrom,
                showState = items.prfstate,
                price = items.pcseguidance,
                cast = items.prfcast,
                time = items.prfTime,
                styUrl = items.styurls?.let { ShowItem.StyUrls(styUrlList = it.styUrlList) },
                relateList = items.relates?.relatesList?.map { relate ->
                    ShowItem.Relate(
                        relateName = relate.relateName,
                        relateUrl = relate.relateUrl
                    )
                } ?: emptyList()
            )
        }.orEmpty()

    private fun createLocationItem(location: DbsEntity<DbsShowListEntity>): List<ShowItem.LocationItem> =
        location.showList?.map { items ->
            ShowItem.LocationItem(
                facilityName = items.prfFacility,
                address = items.adres,
                telno = items.telno,
                relateUrl = items.relateurl,
                latitude = items.la,
                longitude = items.lo,
                showId = items.showId,
                title = items.performanceName,
                placeName = items.facilityName,
                genre = items.genreName,
                posterPath = items.posterPath
            )
        }.orEmpty()

    fun fetchDetailLocation() {
        viewModelScope.launch {
            runCatching {
                createLocationItem(fetch.getLocationList(path = facilityId))
            }.onSuccess { result ->
                _locationList.value = result
            }.onFailure {
                Log.e("DetailViewModel", "fetchDetailLocation: ${it.message}")
                _locationList.value = emptyList()
            }
        }
    }

}
