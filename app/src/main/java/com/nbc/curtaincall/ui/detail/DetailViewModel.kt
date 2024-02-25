package com.nbc.curtaincall.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DetailViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Detail Fragment"
    }
    val text: LiveData<String> = _text

}