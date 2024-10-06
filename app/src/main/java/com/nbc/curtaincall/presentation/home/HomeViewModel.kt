package com.nbc.curtaincall.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.FetchUiState
import com.nbc.curtaincall.ViewType
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopRankUseCase: GetTopRankUseCase,
    private val getGenreUseCase: GetGenreUseCase,
    private val getUpComingUseCase: GetUpComingUseCase,
    private val getKidShowUseCase: GetKidShowUseCase,
) : ViewModel() {
    private val _topRankUiState = MutableStateFlow(FetchUiState.init())
    val topRankUiState: StateFlow<FetchUiState> = _topRankUiState.asStateFlow()

    private val _genreUiState = MutableStateFlow(FetchUiState.init())
    val genreUiState: StateFlow<FetchUiState> = _genreUiState.asStateFlow()

    private val _upComingShowUiState = MutableStateFlow(FetchUiState.init())
    val upComingShowUiState: StateFlow<FetchUiState> = _upComingShowUiState.asStateFlow()

    private val _kidShowUiState = MutableStateFlow(FetchUiState.init())
    val kidShowUiState: StateFlow<FetchUiState> = _kidShowUiState.asStateFlow()

    init {
        fetchTopRank()
        fetchGenre(0)
        fetchUpcoming()
        fetchKidShow()
    }

    private fun fetchTopRank() {
        viewModelScope.launch {
            showLoading(ViewType.TOP_RANK, true)
            runCatching {
                val newList = createTopRankItems(
                    getTopRankUseCase(
                        dateCode = "week",
                        date = Converter.nowDateOneDayAgo(),
                        genreCode = null
                    )
                )
                _topRankUiState.update { prevState ->
                    prevState.copy(list = newList, isLoading = false, viewType = ViewType.TOP_RANK)
                }
            }.onFailure {
                showLoading(ViewType.TOP_RANK, false)
            }
        }
    }

    fun fetchGenre(genre: Int) {
        viewModelScope.launch {
            showLoading(ViewType.GENRE, true)
            runCatching {
                val newList = createGenreItems(
                    getGenreUseCase(
                        genreCode = getGenreCode(genre),
                        kidState = null,
                        showState = "02",
                    )
                )
                _genreUiState.update { prevState ->
                    prevState.copy(list = newList, isLoading = false, viewType = ViewType.GENRE)
                }
            }.onFailure {
                showLoading(ViewType.GENRE, false)
            }
        }
    }

    private fun fetchUpcoming() {
        showLoading(ViewType.UPCOMING_SHOW, true)
        viewModelScope.launch {
            runCatching {
                val newList = createUpcomingShowItems(
                    getUpComingUseCase(
                        showState = "01",
                        genreCode = null,
                        kidState = "N",
                    )
                )
                _upComingShowUiState.update { prevState ->
                    prevState.copy(
                        list = newList,
                        isLoading = false,
                        viewType = ViewType.UPCOMING_SHOW
                    )
                }
            }.onFailure {
                showLoading(ViewType.UPCOMING_SHOW, false)
            }
        }
    }

    private fun fetchKidShow() {
        showLoading(ViewType.KID_SHOW, true)
        viewModelScope.launch {
            runCatching {
                val newList = createKidShowItems(
                    getKidShowUseCase(
                        kidState = "Y",
                        genreCode = null,
                        showState = "01",
                    )
                )
                _kidShowUiState.update { prevState ->
                    prevState.copy(list = newList, isLoading = false, viewType = ViewType.KID_SHOW)
                }
            }.onFailure {
                showLoading(ViewType.KID_SHOW, false)
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

    private fun showLoading(viewType: ViewType, isLoading: Boolean) {
        when (viewType) {
            ViewType.TOP_RANK -> {
                _topRankUiState.update { prevState ->
                    prevState.copy(isLoading = isLoading)
                }
            }

            ViewType.GENRE -> {
                _genreUiState.update { prevState ->
                    prevState.copy(isLoading = isLoading)
                }
            }

            ViewType.UPCOMING_SHOW -> {
                _upComingShowUiState.update { prevState ->
                    prevState.copy(isLoading = isLoading)
                }
            }

            ViewType.KID_SHOW -> {
                _kidShowUiState.update { prevState ->
                    prevState.copy(isLoading = isLoading)
                }
            }

            else -> {}
        }
    }
}


