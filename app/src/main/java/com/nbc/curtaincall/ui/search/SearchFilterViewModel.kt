package com.nbc.curtaincall.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.curtaincall.search.model.SearchItem

class SearchFilterViewModel : ViewModel() {

    private val _addrFilterResultList = MutableLiveData<List<SearchItem>>()
    val addrFilterResultList: LiveData<List<SearchItem>> get() = _addrFilterResultList

    private  val _genreFilterResultList = MutableLiveData<List<SearchItem>>()
    val genreFilterResultList : LiveData<List<SearchItem>> get() = _genreFilterResultList

    private val _childrenFilterResultList = MutableLiveData<List<SearchItem>>()
    val childrenFilterResultList : LiveData<List<SearchItem>> get() = _childrenFilterResultList



}