package com.nbc.curtaincall.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.usecase.GetShowDetailUseCase
import com.nbc.curtaincall.presentation.model.ShowItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getShowDetailUseCase: GetShowDetailUseCase) :
    ViewModel() {
    //공연 상세 정보 리스트
    private val _showDetailInfo = MutableLiveData<List<ShowItem.DetailShowItem>>()
    val showDetailInfo: LiveData<List<ShowItem.DetailShowItem>> get() = _showDetailInfo

    //공연 id
    private val _showId = MutableLiveData<String>()
    val showId: LiveData<String> get() = _showId

    var reserveInfoList = listOf<ShowItem.Relate>()


    //id를 공유 하기 위한 함수
    fun sharedShowId(id: String) {
        _showId.value = id
    }

    //공연 상세 정보를 받아옴
    fun fetchShowDetail(id: String) {
        viewModelScope.launch {
            runCatching { createDetailItem(getShowDetailUseCase(path = id)) }
                .onSuccess { result -> _showDetailInfo.value = result }
                .onFailure { _showDetailInfo.value = emptyList() }
        }
    }

    private fun createDetailItem(detail: DbsEntity<DbsShowListEntity>): List<ShowItem.DetailShowItem> =
        detail.showList?.map { items ->
            ShowItem.DetailShowItem(
                showId = items.showId,
                facilityId = items.prfFacility,
                title = items.performanceName,
                placeName = items.facilityName,
                genre = items.genreName,
                posterPath = items.posterPath,
                age = items.prfAge,
                price = items.pcseguidance,
                showState = items.prfstate,
                periodFrom = items.prfpdfrom,
                periodTo = items.prfpdto,
                time = items.prfTime,
                cast = items.prfcast,
                productCast = items.entrpsnm,
                styUrl = items.styurls?.let { ShowItem.StyUrls(styUrlList = it.styUrlList) },
                relateList = items.relates?.relatesList?.map { relate ->
                    ShowItem.Relate(relateName = relate.relateName, relateUrl = relate.relateUrl)
                } ?: emptyList(),
            )
        }.orEmpty()

    fun shareReserveInfo(reserveInfoList: List<ShowItem.Relate>) {
        this.reserveInfoList = reserveInfoList
    }
}