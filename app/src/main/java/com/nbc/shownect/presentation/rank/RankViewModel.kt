package com.nbc.shownect.presentation.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbc.shownect.fetch.model.BoxofResponse
import com.nbc.shownect.fetch.repository.impl.FetchRepositoryImpl
import kotlinx.coroutines.launch

class RankViewModel(private val fetchRemoteRepository: FetchRepositoryImpl) : ViewModel() {
    private val _rankList = MutableLiveData<List<BoxofResponse>?>()
    val rankList: LiveData<List<BoxofResponse>?> get() = _rankList

    fun fetchInitRank() {
        viewModelScope.launch {
            _rankList.value = fetchRemoteRepository.fetchTopRank(ststype = "day").boxof
        }
    }

    fun fetchPeriod(selectedPeriod: String, selectedGenre: String) {
        viewModelScope.launch {
            _rankList.value =
                fetchRemoteRepository.fetchTopRank(
                    ststype = getPeriod(selectedPeriod),
                    catecode = getGenre(selectedGenre)
                ).boxof
        }
    }
    fun fetchGenre(selectedPeriod: String,selectedGenre: String){
        viewModelScope.launch {
            _rankList.value =
                fetchRemoteRepository.fetchTopRank(
                    ststype = getPeriod(selectedPeriod),
                    catecode = getGenre(selectedGenre)
                ).boxof
        }
    }
    private fun getPeriod(period: String): String {
        return when (period) {
            "일간" -> "day"
            "주간" -> "week"
            "월간" -> "month"
            else -> "day"
        }
    }

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