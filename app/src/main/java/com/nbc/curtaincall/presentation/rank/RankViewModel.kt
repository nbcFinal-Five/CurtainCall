package com.nbc.curtaincall.presentation.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.domain.model.BoxOfficeListEntity
import com.nbc.curtaincall.domain.model.BoxOfsEntity
import com.nbc.curtaincall.domain.usecase.GetTopRankUseCase
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.util.Converter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankViewModel @Inject constructor(private val getTopRankUseCase: GetTopRankUseCase) :
    ViewModel() {
    private val _isRankLoading = MutableLiveData<Boolean>(true)
    val isRankLoading: LiveData<Boolean> get() = _isRankLoading

    private val _rankList = MutableLiveData<List<ShowItem.TopRankItem>>()
    val rankList: MutableLiveData<List<ShowItem.TopRankItem>> get() = _rankList


    init {
        viewModelScope.launch {
            _isRankLoading.value = true
            runCatching {
                getTopRankUseCase(
                    dateCode = "day",
                    date = Converter.nowDateOneDayAgo(),
                    genreCode = getGenre(""),
                )
            }.onSuccess { result ->
                _rankList.value = createRankItems(result)
            }.onFailure {
                _rankList.value = emptyList()
            }.also {
                _isRankLoading.value = false
            }
        }
    }

    private fun createRankItems(
        rank: BoxOfsEntity<BoxOfficeListEntity>
    ): List<ShowItem.TopRankItem> = rank.boxOfficeList?.map { items ->
        ShowItem.TopRankItem(
            showId = items.showId,
            rankNum = items.rankNum,
            title = items.prfName,
            placeName = items.prfplcnm,
            genre = items.genre,
            posterPath = items.poster,
            period = items.prfPeriod
        )
    }.orEmpty()

    //장르 선택시
    fun fetchRank(selectedPeriod: String, selectedGenre: String) {
        viewModelScope.launch {
            _isRankLoading.value = true
            runCatching {
                getTopRankUseCase(
                    dateCode = getPeriod(selectedPeriod),
                    genreCode = getGenre(selectedGenre),
                    date = Converter.nowDateOneDayAgo()
                )
            }.onSuccess { result ->
                _rankList.value = createRankItems(result)

            }.onFailure {
                _rankList.value = emptyList()
            }.also {
                _isRankLoading.value = false
            }
        }
    }

    //컨버트 함수
    private fun getPeriod(period: String): String {
        return when (period) {
            "일간" -> "day"
            "주간" -> "week"
            "월간" -> "month"
            else -> "day"
        }
    }

    //컨버트 함수
    private fun getGenre(genre: String): String {
        return when (genre) {
            "전체" -> ""
            "연극" -> "AAAA"
            "뮤지컬" -> "GGGA"
            "클래식" -> "CCCA"
            "무용" -> "BBBC|BBBE"
            "국악" -> "CCCC"
            "대중음악" -> "CCCD"
            "서커스/마술" -> "EEEB"
            "복합" -> "EEEA"
            else -> ""
        }
    }

}
