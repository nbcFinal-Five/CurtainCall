package com.nbc.curtaincall.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.data.model.KopisApiInterface

class HomeViewModelFactory(private val kopisApi: KopisApiInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(kopisApi) as T
    }
}