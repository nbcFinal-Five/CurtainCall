package com.nbc.curtaincall.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.fetch.remote.FetchRemoteDatasource

class HomeViewModelFactory(
    private val fetchRemoteRepository: FetchRemoteDatasource,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(fetchRemoteRepository) as T
    }
}