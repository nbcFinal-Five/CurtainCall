package com.nbc.curtaincall.ui.main

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nbc.curtaincall.R
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.usecase.GetShowDetailUseCase
import com.nbc.curtaincall.presentation.Key.Companion.DB_BOOKMARKS
import com.nbc.curtaincall.presentation.Key.Companion.DB_USERS
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.presentation.ticket.ReserveFragment
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

    private val _myPageInterestList = MutableLiveData<List<ShowItem.DetailShowItem>>()
    val myPageInterestList get() = _myPageInterestList

    private val _isBookmarkItemExist = MutableLiveData<Boolean>()
    val isBookmarkItemExist get() = _isBookmarkItemExist

    private val _isBookmarked = MutableLiveData<Boolean>()
    val isBookmarked get() = _isBookmarked

    init {
        fetchMyPageInterestList()
    }

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

    fun shareReserveInfo(reserveInfoList: List<ShowItem.Relate>, fragmentManager: FragmentManager) {
        this.reserveInfoList = reserveInfoList
        val reserveDialog = ReserveFragment()
        reserveDialog.setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.RoundedBottomSheetDialog
        )
        reserveDialog.show(fragmentManager, reserveDialog.tag)
    }

    fun bookMark(showId: String, isBookmarked: (Boolean) -> Unit) {
        val database = Firebase.firestore
        val uid = Firebase.auth.currentUser?.uid ?: return
        //TODO 로그인 필요 알림 처리 로그인 유도 팝업
        val showRef =
            database.collection(DB_USERS).document(uid).collection(DB_BOOKMARKS).document(showId)
        showRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                showRef.delete().addOnSuccessListener {
                    isBookmarked(false)
                    _isBookmarked.value = false
                    fetchMyPageInterestList()
                }
            } else {
                showDetailInfo.value?.map {
                    val show = hashMapOf(
                        "showId" to it.showId,
                        "title" to it.title,
                        "age" to it.age,
                        "place" to it.placeName,
                        "genre" to it.genre,
                        "showState" to it.showState,
                        "castSub" to it.cast,
                        "posterPath" to it.posterPath,
                        "area" to it.area,
                        "facilityId" to it.facilityId,
                        "reserveInfo" to it.relateList,
                        "addDate" to Timestamp.now(),
                        "runTime" to it.runTime,
                        "price" to it.price,
                        "periodFrom" to it.periodFrom,
                        "periodTo" to it.periodTo,
                        "time" to it.time,
                        "productCase" to it.productCast,
                        "styUrl" to it.styUrl,
                    )
                    showRef.set(show).addOnSuccessListener {
                        isBookmarked(true)
                        _isBookmarked.value = true
                        fetchMyPageInterestList()
                    }
                }
            }
        }
    }

    private fun fetchMyPageInterestList() {
        val database = Firebase.firestore
        val uid = Firebase.auth.currentUser?.uid ?: ""
        database.collection(DB_USERS).document(uid).collection(DB_BOOKMARKS).get()
            .addOnSuccessListener { documents ->
                val interestList = documents.map { documentSnapshot ->
                    val showData = documentSnapshot.data
                    ShowItem.DetailShowItem(
                        runTime = showData["runTime"] as? String,
                        showId = showData["showId"] as? String,
                        facilityId = showData["facilityId"] as? String,
                        title = showData["title"] as? String,
                        placeName = showData["place"] as? String,
                        genre = showData["genre"] as? String,
                        posterPath = showData["posterPath"] as? String,
                        age = showData["age"] as? String,
                        price = showData["price"] as? String,
                        showState = showData["showState"] as? String,
                        periodFrom = showData["periodFrom"] as? String,
                        periodTo = showData["periodTo"] as? String,
                        time = showData["time"] as? String,
                        cast = showData["castSub"] as? String,
                        productCast = showData["productCast"] as? String,
                        styUrl = showData["styUrl"] as? ShowItem.StyUrls,
                        relateList = showData["relateList"] as? List<ShowItem.Relate>,
                        area = showData["area"] as? String
                    )
                }
                if (interestList.isNotEmpty()) {
                    _myPageInterestList.value = interestList
                } else {
                    _myPageInterestList.value = emptyList()
                }
                _isBookmarkItemExist.value = _myPageInterestList.value?.size != 0
            }
    }
}