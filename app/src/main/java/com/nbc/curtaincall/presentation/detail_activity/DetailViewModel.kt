package com.nbc.curtaincall.ui.detail_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.usecase.AddBookmarkUseCase
import com.nbc.curtaincall.domain.usecase.CheckBookmarkUseCase
import com.nbc.curtaincall.domain.usecase.GetLocationUseCase
import com.nbc.curtaincall.domain.usecase.GetShowDetailUseCase
import com.nbc.curtaincall.domain.usecase.RemoveBookmarkUseCase
import com.nbc.curtaincall.presentation.model.ShowItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getShowDetailUseCase: GetShowDetailUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val checkBookmarkUseCase: CheckBookmarkUseCase,
    private val getLocationUseCase: GetLocationUseCase
) :
    ViewModel() {
    private lateinit var showId: String //공연 id
    private lateinit var facilityId: String //공연장 id

    private val _detailInfoList = MutableLiveData<List<ShowItem.DetailShowItem>>()
    val detailInfoList: LiveData<List<ShowItem.DetailShowItem>> get() = _detailInfoList

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
                val isBookmarked = checkBookmarkUseCase(showId)
                createItem(getShowDetailUseCase(path = showId), isBookmarked)
            }.onSuccess { result ->
                _detailInfoList.value = result
            }.onFailure { _detailInfoList.value = emptyList() }
        }
    }

    private fun createItem(
        showItem: DbsEntity<DbsShowListEntity>,
        isBookmarked: Boolean
    ): List<ShowItem.DetailShowItem> =
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
                } ?: emptyList(),
                area = items.area,
                runTime = items.prfruntime,
                isBookmarked = isBookmarked
            )
        }.orEmpty()

    private fun createLocationItem(location: DbsEntity<DbsShowListEntity>): List<ShowItem.LocationItem> =
        location.showList?.map { items ->
            ShowItem.LocationItem(
                facilityId = items.prfFacility,
                facilityName = items.facilityName,
                address = items.adres,
                telno = items.telno,
                relateUrl = items.relateurl,
                latitude = items.la,
                longitude = items.lo,
                showId = items.showId,
                title = items.performanceName,
                placeName = items.facilityName,
                genre = items.genreName,
                posterPath = items.posterPath,
                seatscale = items.seatscale
            )
        }.orEmpty()

    //현재 파싱 라이브러리 문제로 기능 정지
    fun fetchDetailLocation() {
        viewModelScope.launch {
            runCatching {
                createLocationItem(getLocationUseCase(path = facilityId))
            }.onSuccess { result ->
                _locationList.value = result
            }.onFailure {
                Log.e("DetailViewModel", "fetchDetailLocation: ${it.message}")
                _locationList.value = emptyList()
            }
        }
    }

    fun bookmark(showId: String) {
        viewModelScope.launch {
            val showItem =
                _detailInfoList.value?.firstOrNull { it.showId == showId } ?: return@launch
            toggleBookmark(showItem)
            if (showItem.isBookmarked) {
                removeBookmarkUseCase(showId)
            } else {
                addBookmarkUseCase(showItem)
            }
        }
    }

    private fun toggleBookmark(item: ShowItem) {
        _detailInfoList.value = _detailInfoList.value?.map {
            if (it == item) {
                it.copy(isBookmarked = !it.isBookmarked)
            } else {
                it
            }
        }
    }
}
