package com.nbc.curtaincall.ui.main

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.R
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.usecase.GetShowDetailUseCase
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.ui.ticket.TicketDialogFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getShowDetailUseCase: GetShowDetailUseCase) :
    ViewModel() {
    //공연 상세 정보 리스트
    private val _showDetailInfo = MutableLiveData<List<ShowItem.DetailShowItem>>()
    val showDetailInfo: LiveData<List<ShowItem.DetailShowItem>> get() = _showDetailInfo

    var reserveInfoList = listOf<ShowItem.Relate>()


    fun posterClick(id: String, fragmentManager: FragmentManager) {
        viewModelScope.launch {
            runCatching { createDetailItem(getShowDetailUseCase(path = id)) }
                .onSuccess { result -> _showDetailInfo.value = result }
                .onFailure { _showDetailInfo.value = emptyList() }
        }
        val ticketDialog = TicketDialogFragment()
        ticketDialog.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundCornerBottomSheetDialogTheme
        )
        ticketDialog.show(fragmentManager, ticketDialog.tag)
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
                area = items.area,
                runTime = items.prfruntime,
            )
        }.orEmpty()

    fun shareReserveInfo(reserveInfoList: List<ShowItem.Relate>) {
        this.reserveInfoList = reserveInfoList
    }
}