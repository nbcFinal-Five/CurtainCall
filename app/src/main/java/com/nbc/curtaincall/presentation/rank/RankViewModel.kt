package com.nbc.curtaincall.presentation.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.data.model.BoxofResponse
import com.nbc.curtaincall.domain.repository.FetchRepository
import com.nbc.curtaincall.util.Converter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankViewModel @Inject constructor(private val fetch: FetchRepository) : ViewModel() {
    private val _isRankLoading = MutableLiveData<Boolean>(true)
    val isRankLoading: LiveData<Boolean> get() = _isRankLoading

    private val _rankList = MutableLiveData<List<BoxofResponse>?>()
    val rankList: MutableLiveData<List<BoxofResponse>?> get() = _rankList

    //초기 설정용 상태 값
    private val _initState = MutableLiveData<Boolean>(false)
    val initState: LiveData<Boolean> get() = _initState

    init {
        viewModelScope.launch {
            _isRankLoading.value = true
            runCatching {
                fetch.fetchTopRank(
                    dateCode = "day",
                    date = Converter.nowDateOneDayAgo(),
                    genreCode = getGenre(""),
                )
            }.onSuccess { result ->
                _rankList.value = result
            }.onFailure {
                _rankList.value = emptyList()
            }
                .also {
                    _isRankLoading.value = false
                }
        }
    }

    //장르 선택시
    fun fetchRank(selectedPeriod: String, selectedGenre: String) {
        viewModelScope.launch {
            _isRankLoading.value = true
            runCatching {
                fetch.fetchTopRank(
                    dateCode = getPeriod(selectedPeriod),
                    genreCode = getGenre(selectedGenre),
                    date = Converter.nowDateOneDayAgo()
                )
            }.onSuccess { result ->
                _rankList.value =
                    result
            }.onFailure {
                _rankList.value = emptyList()
            }.also {
                _isRankLoading.value = false
            }
        }
    }

    //초기 화면 세팅용 함수
    fun initState(initState: Boolean) {
        _initState.value = initState
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
