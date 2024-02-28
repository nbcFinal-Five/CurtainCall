package com.nbc.curtaincall.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.fetch.model.BoxofResponse
import com.nbc.curtaincall.fetch.model.DbResponse
import com.nbc.curtaincall.fetch.remote.FetchRemoteDatasource
import com.nbc.curtaincall.util.Constants
import kotlinx.coroutines.launch

class HomeViewModel(
    private val fetchRemoteRepository: FetchRemoteDatasource,
) : ViewModel() {
    private val _showList = MutableLiveData<List<DbResponse>>()
    val showList: LiveData<List<DbResponse>> get() = _showList

    private val _topRank = MutableLiveData<List<BoxofResponse>>()
    val topRank: LiveData<List<BoxofResponse>> get() = _topRank
    private val _genre = MutableLiveData<List<DbResponse>>()
    val genre: LiveData<List<DbResponse>> get() = _genre

    fun fetchUpcomingList() {
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
            }.onFailure {

            }
        }
    }

    fun fetchGenre(genre: Int) {
        viewModelScope.launch {
            runCatching {
                _genre.value = fetchRemoteRepository.fetchGenre(shcate = getGenreCode(genre)).showList
            }.onFailure {
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



