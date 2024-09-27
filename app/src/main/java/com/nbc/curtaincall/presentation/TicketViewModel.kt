package com.nbc.curtaincall.presentation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.nbc.curtaincall.R
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.usecase.AddBookmarkUseCase
import com.nbc.curtaincall.domain.usecase.CheckBookmarkUseCase
import com.nbc.curtaincall.domain.usecase.GetBookmarkUseCase
import com.nbc.curtaincall.domain.usecase.GetShowDetailUseCase
import com.nbc.curtaincall.domain.usecase.RemoveBookmarkUseCase
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.presentation.ticket.ReserveFragment
import com.nbc.curtaincall.ui.ticket.TicketDialogFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val getShowDetailUseCase: GetShowDetailUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val getBookmarksUseCase: GetBookmarkUseCase,
    private val checkBookmarkUseCase: CheckBookmarkUseCase
) :
    ViewModel() {
    //공연 상세 정보 리스트
    private val _showDetailInfo = MutableLiveData<List<ShowItem.DetailShowItem>>()
    val showDetailInfo: LiveData<List<ShowItem.DetailShowItem>> get() = _showDetailInfo

    var reserveInfoList = listOf<ShowItem.Relate>()

    private val _myPageInterestList = MutableLiveData<List<ShowItem.DetailShowItem>>()
    val myPageInterestList get() = _myPageInterestList

    private val _isLogin = MutableStateFlow<Boolean>(false)
    val isLogin get() = _isLogin.asStateFlow()


    fun getBookmarks() {
        viewModelScope.launch {
            getBookmarksUseCase().collect { bookmarks ->
                _myPageInterestList.value = bookmarks
            }
        }
    }

    fun posterClick(id: String, fragmentManager: FragmentManager) {
        viewModelScope.launch {
            runCatching {
                val isBookmarked = checkBookmarkUseCase(id)
                createDetailItem(getShowDetailUseCase(path = id), isBookmarked)
            }.onSuccess { result -> _showDetailInfo.value = result }
                .onFailure { _showDetailInfo.value = emptyList() }
        }
        val ticketDialog = TicketDialogFragment()
        ticketDialog.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundCornerBottomSheetDialogTheme
        )
        ticketDialog.show(fragmentManager, ticketDialog.tag)
    }

    private fun createDetailItem(
        detail: DbsEntity<DbsShowListEntity>,
        isBookmarked: Boolean
    ): List<ShowItem.DetailShowItem> =
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
                isBookmarked = isBookmarked
            )
        }.orEmpty()

    fun shareReserveInfo(reserveInfoList: List<ShowItem.Relate>, fragmentManager: FragmentManager) {
        this.reserveInfoList = reserveInfoList
        val reserveDialog = ReserveFragment()
        reserveDialog.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundedBottomSheetDialog
        )
        reserveDialog.show(fragmentManager, reserveDialog.tag)
    }


    fun bookMark(showId: String) {
        if (Firebase.auth.currentUser != null) {
            viewModelScope.launch {
                val showItem =
                    showDetailInfo.value?.firstOrNull { it.showId == showId } ?: return@launch
                toggleBookmark(showItem)
                if (showItem.isBookmarked) {
                    removeBookmarkUseCase(showId)
                } else {
                    addBookmarkUseCase(showItem)
                }
            }
        } else {
        }
    }

    private fun toggleBookmark(item: ShowItem) {
        val updatedList = _showDetailInfo.value?.toMutableList() ?: mutableListOf()
        val index = updatedList.indexOf(item)
        if (index != -1 && item is ShowItem.DetailShowItem) {
            updatedList[index] = updatedList[index].copy(isBookmarked = !item.isBookmarked)
            _showDetailInfo.value = updatedList
        }
    }

    suspend fun checkBookmark(showId: String): Boolean = checkBookmarkUseCase(showId)

    fun putLoginState(state: Boolean) {
        _isLogin.value = state
    }
}