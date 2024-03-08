package com.nbc.shownect.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nbc.shownect.fetch.model.BoxofResponse
import com.nbc.shownect.fetch.model.DbResponse
import com.nbc.shownect.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.shownect.util.Constants
import kotlinx.coroutines.launch

class HomeViewModel(
    private val fetchRemoteRepository: FetchRepositoryImpl,
) : ViewModel() {
    private val _showList = MutableLiveData<List<DbResponse>?>()
    val showList: LiveData<List<DbResponse>?> get() = _showList


    private val _topRank = MutableLiveData<List<BoxofResponse>?>()
    val topRank: LiveData<List<BoxofResponse>?> get() = _topRank

    private val _genre = MutableLiveData<List<DbResponse>?>()
    val genre: LiveData<List<DbResponse>?> get() = _genre


    private val _kidShow = MutableLiveData<List<DbResponse>?>()
    val kidShow: LiveData<List<DbResponse>?> get() = _kidShow

    private val _isLoadingRecommend = MutableLiveData<Boolean>(false)
    val isLoadingRecommend: LiveData<Boolean> get() = _isLoadingRecommend

    private val _isLoadingGenre = MutableLiveData<Boolean>(false)
    val isLoadingGenre: LiveData<Boolean> get() = _isLoadingGenre

    private val _isLoadingKid = MutableLiveData<Boolean>(false)
    val isLoadingKid: LiveData<Boolean> get() = _isLoadingKid

    private val _tabPosition = MutableLiveData<Int>()
    val tabPosition: LiveData<Int> get() = _tabPosition


    fun fetchUpcoming() {
        viewModelScope.launch {
            runCatching {
                _showList.value = fetchRemoteRepository.fetchShowList().showList
            }.onFailure {

            }
        }
    }

    fun fetchTopRank() {
        viewModelScope.launch {
            runCatching {
                _topRank.value = fetchRemoteRepository.fetchTopRank().boxof
                _isLoadingRecommend.value = true
            }.onFailure {

            }
        }
    }

    fun fetchGenre(genre: Int) {
        viewModelScope.launch {
            runCatching {
                _genre.value =
                    fetchRemoteRepository.fetchGenre(shcate = getGenreCode(genre)).showList
                _isLoadingGenre.value = true
            }.onFailure {
            }
        }
    }

    fun fetchKidShow() {
        viewModelScope.launch {
            runCatching {
                _kidShow.value = fetchRemoteRepository.fetchShowList(
                    stdate = "20240101",
                    eddate = "20240328",
                    kidstate = "Y"
                ).showList
                _isLoadingKid.value = true
            }
        }
    }

    fun saveTabPosition(tabPosition: Int) {
        _tabPosition.value = tabPosition
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

class HomeViewModelFactory(
    private val fetchRemoteRepository: FetchRepositoryImpl,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(fetchRemoteRepository) as T
    }
}


