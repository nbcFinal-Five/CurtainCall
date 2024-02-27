package com.nbc.curtaincall.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.curtaincall.data.model.Boxof
import com.nbc.curtaincall.data.model.Db
import com.nbc.curtaincall.data.repository.BoxOfficeRepository
import com.nbc.curtaincall.data.repository.ShowListRepository
import com.nbc.curtaincall.util.Constants
import kotlinx.coroutines.launch

class HomeViewModel(
    private val showListRepository: ShowListRepository,
    private val boxOfficeRepository: BoxOfficeRepository
) : ViewModel() {
    private val _showList = MutableLiveData<List<Db>>()
    val showList: LiveData<List<Db>> get() = _showList

    private val _topRank = MutableLiveData<List<Boxof>>()
    val topRank: LiveData<List<Boxof>> get() = _topRank
    private val _genre = MutableLiveData<List<Db>>()
    val genre: LiveData<List<Db>> get() = _genre

    fun fetchUpcomingList() {
        viewModelScope.launch {
            runCatching {
                _showList.value = showListRepository.fetchShowList().showList
            }.onFailure {

            }
        }
    }

    fun fetchTopRank() {
        viewModelScope.launch {
            runCatching {
                _topRank.value = boxOfficeRepository.fetchTopRank().boxof
            }.onFailure {

            }
        }
    }

    fun fetchGenre(genre: Int) {
        viewModelScope.launch {
            runCatching {
                _genre.value = showListRepository.fetchGenre(shcate = getGenreCode(genre)).showList
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



