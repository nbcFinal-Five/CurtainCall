package com.nbc.curtaincall.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.domain.model.BoxOfficeListEntity
import com.nbc.curtaincall.domain.model.BoxOfsEntity
import com.nbc.curtaincall.domain.model.DbsEntity
import com.nbc.curtaincall.domain.model.DbsShowListEntity
import com.nbc.curtaincall.domain.usecase.GetGenreUseCase
import com.nbc.curtaincall.domain.usecase.GetKidShowUseCase
import com.nbc.curtaincall.domain.usecase.GetTopRankUseCase
import com.nbc.curtaincall.domain.usecase.GetUpComingUseCase
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.util.Constants
import com.nbc.curtaincall.util.Converter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopRankUseCase: GetTopRankUseCase,
    private val getGenreUseCase: GetGenreUseCase,
    private val getUpComingUseCase: GetUpComingUseCase,
    private val getKidShowUseCase: GetKidShowUseCase
) :
    ViewModel() {
    private val _topRank = MutableLiveData<List<ShowItem>>()
    val topRank: LiveData<List<ShowItem>> get() = _topRank

    private val _genre = MutableLiveData<List<ShowItem>>()
    val genre: LiveData<List<ShowItem>> get() = _genre

    private val _showList = MutableLiveData<List<ShowItem>>()
    val showList: LiveData<List<ShowItem>> get() = _showList

    private val _kidShow = MutableLiveData<List<ShowItem>>()
    val kidShow: LiveData<List<ShowItem>> get() = _kidShow

    private val _isLoadingRecommend = MutableLiveData<Boolean>(true)
    val isLoadingRecommend: LiveData<Boolean> get() = _isLoadingRecommend

    private val _isLoadingGenre = MutableLiveData<Boolean>(true)
    val isLoadingGenre: LiveData<Boolean> get() = _isLoadingGenre

    private val _isLoadingKid = MutableLiveData<Boolean>(true)
    val isLoadingKid: LiveData<Boolean> get() = _isLoadingKid

    private val _isServerErrorViewPager = MutableLiveData<Boolean>(false)
    val isServerErrorViewPager: LiveData<Boolean> get() = _isServerErrorViewPager

    private val _isServerErrorTopRank = MutableLiveData<Boolean>(false)
    val isServerErrorTopRank: LiveData<Boolean> get() = _isServerErrorTopRank

    private val _isServerErrorGenre = MutableLiveData<Boolean>(false)
    val isServerErrorGenre: LiveData<Boolean> get() = _isServerErrorGenre

    private val _isServerErrorKid = MutableLiveData<Boolean>(false)
    val isServerErrorKid: LiveData<Boolean> get() = _isServerErrorKid


    init {
        viewModelScope.launch {
            runCatching {
                _isLoadingGenre.value = true
                _isLoadingGenre.value = true
                _isLoadingKid.value = true
                fetchTopRank()
                fetchGenre(0)
                fetchUpcoming()
                fetchKidShow()
            }.onFailure {
                _topRank.value = emptyList()
                _genre.value = emptyList()
                _showList.value = emptyList()
                _kidShow.value = emptyList()
            }.also {
                _isLoadingGenre.value = false
                _isLoadingGenre.value = false
                _isLoadingKid.value = false
            }
        }
    }

    private fun fetchTopRank() {
        viewModelScope.launch {
            runCatching {
                getTopRankUseCase(
                    dateCode = "week",
                    date = Converter.nowDateOneDayAgo(),
                    genreCode = null,
                )
            }.onSuccess { result ->
                _topRank.value = createTopRankItems(result)
                _isLoadingRecommend.value = false
            }.onFailure {
                _isServerErrorTopRank.value = true
                _isLoadingRecommend.value = false
            }
        }
    }

    fun fetchGenre(genre: Int) {
        viewModelScope.launch {
            runCatching {
                getGenreUseCase(
                    genreCode = getGenreCode(genre),
                    kidState = null,
                    showState = "02",
                )
            }.onSuccess { result ->
                _genre.value = createGenreItems(result)
                _isLoadingGenre.value = false
                _isServerErrorGenre.value = false
            }.onFailure {
                _isServerErrorGenre.value = true
                _isLoadingGenre.value = false
            }
        }
    }

    private fun fetchUpcoming() {
        viewModelScope.launch {
            runCatching {
                getUpComingUseCase(
                    showState = "01",
                    genreCode = null,
                    kidState = "N"
                )
            }.onSuccess { result ->
                _showList.value = createUpcomingShowItems(result)
            }.onFailure {
                _isServerErrorViewPager.value = true
            }
        }
    }


    private fun fetchKidShow() {
        viewModelScope.launch {
            runCatching {
                getKidShowUseCase(
                    kidState = "Y",
                    genreCode = null,
                    showState = "01",
                )
            }.onSuccess { result ->
                _kidShow.value = createKidShowItems(result)
                _isLoadingKid.value = false
            }.onFailure {
                _isServerErrorKid.value = true
                _isLoadingKid.value = false
            }
        }
    }

    private fun createTopRankItems(
        topRank: BoxOfsEntity<BoxOfficeListEntity>
    ): List<ShowItem.TopRankItem> = topRank.boxOfficeList?.map { items ->
        ShowItem.TopRankItem(
            showId = items.showId,
            rankNum = items.rankNum,
            title = items.prfName,
            placeName = items.prfplcnm,
            genre = items.genre,
            posterPath = items.poster,
            period = items.prfPeriod,
        )
    }.orEmpty()

    private fun createGenreItems(
        genre: DbsEntity<DbsShowListEntity>
    ): List<ShowItem.GenreItem> = genre.showList?.map { items ->
        ShowItem.GenreItem(
            showId = items.showId,
            title = items.performanceName,
            placeName = items.facilityName,
            genre = items.genreName,
            posterPath = items.posterPath,
            showingState = items.prfstate,
            periodTo = items.prfpdto,
            periodFrom = items.prfpdfrom,
            facilityId = items.prfFacility
        )
    }.orEmpty()

    private fun createUpcomingShowItems(
        upComingShow: DbsEntity<DbsShowListEntity>
    ): List<ShowItem.UpcomingShowItem> = upComingShow.showList?.map { items ->
        ShowItem.UpcomingShowItem(
            showId = items.showId,
            title = items.performanceName,
            placeName = items.facilityName,
            genre = items.genreName,
            posterPath = items.posterPath,
            showingState = items.prfstate,
            periodTo = items.prfpdto,
            periodFrom = items.prfpdfrom,
            facilityId = items.prfFacility
        )
    }.orEmpty()

    private fun createKidShowItems(
        kidShow: DbsEntity<DbsShowListEntity>
    ): List<ShowItem.KidShowItem> = kidShow.showList?.map { items ->
        ShowItem.KidShowItem(
            showId = items.showId,
            title = items.performanceName,
            placeName = items.facilityName,
            genre = items.genreName,
            posterPath = items.posterPath,
            showingState = items.prfstate,
            periodTo = items.prfpdto,
            periodFrom = items.prfpdfrom,
            facilityId = items.prfFacility
        )
    }.orEmpty()


    private fun getGenreCode(genre: Int): String {
        return when (genre) {
            0 -> Constants.DRAMA
            1 -> Constants.DANCE
            2 -> Constants.POPULAR_DANCE
            3 -> Constants.CLASSIC
            4 -> Constants.KOREA_MUSIC
            5 -> Constants.POPULAR_MUSIC
            6 -> Constants.COMPLEX
            7 -> Constants.CIRCUS_MAGIC
            8 -> Constants.MUSICAL
            else -> Constants.DRAMA
        }
    }
}


