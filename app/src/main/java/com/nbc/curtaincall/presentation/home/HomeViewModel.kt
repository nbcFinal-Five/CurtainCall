package com.nbc.curtaincall.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.data.model.BoxofResponse
import com.nbc.curtaincall.data.model.DbResponse
import com.nbc.curtaincall.domain.repository.FetchRepository
import com.nbc.curtaincall.util.Constants
import com.nbc.curtaincall.util.Converter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val fetch: FetchRepository) : ViewModel() {
    private val _showList = MutableLiveData<List<DbResponse>?>()
    val showList: LiveData<List<DbResponse>?> get() = _showList


    private val _topRank = MutableLiveData<List<BoxofResponse>?>()
    val topRank: LiveData<List<BoxofResponse>?> get() = _topRank

    private val _genre = MutableLiveData<List<DbResponse>?>()
    val genre: LiveData<List<DbResponse>?> get() = _genre


    private val _kidShow = MutableLiveData<List<DbResponse>?>()
    val kidShow: LiveData<List<DbResponse>?> get() = _kidShow

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
    fun fetchUpcoming() {
        viewModelScope.launch {
            runCatching {
                _showList.value = fetch.fetchShowList(
                    showState = "02",
                    newSQL = "Y",
                    genreCode = null,
                    kidState = "N"
                ).showList
            }.onFailure {
                _isServerErrorViewPager.value = true
            }
        }
    }

    fun fetchTopRank() {
        viewModelScope.launch {
            runCatching {
                _topRank.value = fetch.fetchTopRank(
                    dateCode = "week",
                    date = Converter.nowDateOneDayAgo(),
                    genreCode = null,
                    area = "11",
                    newSQL = "Y"
                ).boxof
            }.onSuccess {
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
                _genre.value =
                    fetch.fetchShowList(
                        genreCode = getGenreCode(genre),
                        kidState = "N",
                        showState = "01",
                        newSQL = "Y"
                    ).showList
            }.onSuccess {
                _isLoadingGenre.value = false
                _isServerErrorGenre.value = false
            }.onFailure {
                _isServerErrorGenre.value = true
                _isLoadingGenre.value = false
            }
        }
    }

    fun fetchKidShow() {
        viewModelScope.launch {
            runCatching {
                _kidShow.value = fetch.fetchShowList(
                    kidState = "Y",
                    newSQL = "Y",
                    genreCode = null,
                    showState = "01",
                ).showList
            }.onSuccess {
                _isLoadingKid.value = false
            }.onFailure {
                _isServerErrorKid.value = true
                _isLoadingKid.value = false
            }
        }
    }


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


