package com.nbc.shownect.ui.detail_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.shownect.fetch.model.DbResponse
import com.nbc.shownect.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.shownect.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.shownect.supabase.Supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {
	private val fetchRemoteRepository: FetchRepositoryImpl = FetchRepositoryImpl(fetch = fetch)
	private lateinit var showId: String //공연 id
	private lateinit var facilityId: String //공연장 id
	private val _detailInfoList = MutableLiveData<List<DbResponse>?>()
	val detailInfoList: MutableLiveData<List<DbResponse>?> get() = _detailInfoList

	private var _totalExpectationCount: MutableLiveData<Int> = MutableLiveData(0)
	val totalExpectationCount: LiveData<Int>
		get() = _totalExpectationCount
	
	fun sharedId(mt20Id: String, mt10Id: String) {
		showId = mt20Id
		facilityId = mt10Id
	}

	fun setExpectationCount(mt20id: String) {
		CoroutineScope(Dispatchers.IO).launch {
			val count = Supabase.client
				.from("expectations")
				.select {
					filter {
						eq(column = "mt20id", value = mt20id)
					}
					count(Count.EXACT)
				}.countOrNull()!!

			withContext(Dispatchers.Main) { _totalExpectationCount.value = count.toInt() }
		}
	}

	fun fetchDetailInfo() {
		viewModelScope.launch {
			runCatching {
				_detailInfoList.value = fetch.fetchShowDetail(path = showId).showList
			}
		}
	}
}
