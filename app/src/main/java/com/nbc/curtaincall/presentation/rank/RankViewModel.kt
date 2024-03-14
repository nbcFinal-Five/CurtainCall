package com.nbc.curtaincall.presentation.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.fetch.model.BoxofResponse
import com.nbc.curtaincall.fetch.repository.impl.FetchRepositoryImpl
import kotlinx.coroutines.launch

class RankViewModel(private val fetchRemoteRepository: FetchRepositoryImpl) : ViewModel() {
    private val _rankInitList = MutableLiveData<List<BoxofResponse>?>()
    val rankInitList: LiveData<List<BoxofResponse>?> get() = _rankInitList
    private val _isRankLoading = MutableLiveData<Boolean>(true)
    val isRankLoading: LiveData<Boolean> get() = _isRankLoading

    private val _rankList = MutableLiveData<List<BoxofResponse>?>()
    val rankList: MutableLiveData<List<BoxofResponse>?> get() = _rankList

    //초기 설정용 상태 값
    private val _initState = MutableLiveData<Boolean>(false)
    val initState: LiveData<Boolean> get() = _initState

    //초기 화면 세팅
    fun fetchInitRank() {
        viewModelScope.launch {
            runCatching {
                _isRankLoading.value = true
                _rankInitList.value = fetchRemoteRepository.fetchTopRank(
                    ststype = "day",
                ).boxof
                _isRankLoading.value = false
            }
        }
    }

    //장르 선택시
    fun fetchRank(selectedPeriod: String, selectedGenre: String) {
        viewModelScope.launch {
            runCatching {
                _isRankLoading.value = true
                _rankList.value =
                    fetchRemoteRepository.fetchTopRank(
                        ststype = getPeriod(selectedPeriod),
                        catecode = getGenre(selectedGenre)
                    ).boxof
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

class RankViewModelFactory(
    private val fetchRemoteRepository: FetchRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RankViewModel(fetchRemoteRepository) as T
    }
}