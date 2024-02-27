package com.nbc.curtaincall.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.data.repository.BoxOfficeRepository
import com.nbc.curtaincall.data.repository.ShowListRepository

class HomeViewModelFactory(private val showListRepository: ShowListRepository,private val boxOfficeRepository: BoxOfficeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(showListRepository,boxOfficeRepository) as T
    }
}