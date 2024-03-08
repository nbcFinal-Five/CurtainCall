package com.nbc.shownect.ui.detail_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nbc.shownect.fetch.model.DbResponse
import com.nbc.shownect.fetch.network.retrofit.RetrofitClient.fetch
import com.nbc.shownect.fetch.repository.impl.FetchRepositoryImpl
import com.nbc.shownect.supabase.Supabase
import com.nbc.shownect.supabase.model.PostBookmarkModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class DetailViewModel : ViewModel() {
	private val fetchRemoteRepository: FetchRepositoryImpl = FetchRepositoryImpl(fetch = fetch)
	private lateinit var showId: String //공연 id
	private lateinit var facilityId: String //공연장 id
	private val _detailInfoList = MutableLiveData<List<DbResponse>?>()
	val detailInfoList: LiveData<List<DbResponse>?> get() = _detailInfoList

	private var _totalExpectationCount: MutableLiveData<Int> = MutableLiveData(0)
	val totalExpectationCount: LiveData<Int>
		get() = _totalExpectationCount

	private var _point: MutableLiveData<Double> = MutableLiveData(0.0)
	val point: LiveData<Double>
		get() = _point

	private var _isBookmark: MutableLiveData<Boolean> = MutableLiveData(false)
	val isBookmark: LiveData<Boolean>
		get() = _isBookmark

	private val _locationList = MutableLiveData<List<DbResponse>>()
	val locationList: LiveData<List<DbResponse>>
		get() = _locationList

	fun sharedId(mt20Id: String, mt10Id: String) {
		showId = mt20Id
		facilityId = mt10Id
	}

	fun fetchDetailInfo() {
		viewModelScope.launch {
			runCatching {
				_detailInfoList.value =
					fetchRemoteRepository.fetchShowDetail(path = showId).showList
			}
		}
	}

	fun setInfo(mt20id: String) {
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

		CoroutineScope(Dispatchers.IO).launch {
			try {
				val point = Supabase.client.postgrest.rpc(
					function = "get_average_point",
					parameters = buildJsonObject {
						put("mt20id_arg", mt20id)
					}
				).data.toDouble()

				withContext(Dispatchers.Main) {
					_point.value = point
				}
			} catch (e: RestException) {
				Log.d("set point", e.error)
			}
		}
	}

	fun setIsLike(mt20id: String, userId: String?) {
		if (userId == null) {
			_isBookmark.value = false
			return
		}

		CoroutineScope(Dispatchers.IO).launch {
			val result = Supabase.client.postgrest.rpc(
				function = "check_bookmark_exists",
				parameters = buildJsonObject {
					put("mt20id_arg", mt20id)
					put("user_id_arg", userId)
				}
			).data.toBoolean()

			withContext(Dispatchers.Main) { _isBookmark.value = result }
		}
	}

	fun fetchDetailLocation() {
		viewModelScope.launch {
			runCatching {
				_locationList.value = fetch.getLocationList(path = facilityId).showList!!
				Log.d("ViewModel", "fetchDetailLocation: ${locationList}")
			}.onFailure {
				Log.e("DetailViewModel", "fetchDetailLocation: ${it.message}")
			}
		}
	}

	fun createBookmark(mt20id: String, mt10id: String, userId: String, poster: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val bookmarkModel = PostBookmarkModel(
					mt20id = mt20id,
					userId = userId,
					poster = poster,
					mt10id = mt10id
				)

				Supabase.client
					.from("bookmarks")
					.insert<PostBookmarkModel>(bookmarkModel)

				withContext(Dispatchers.Main) {
					setIsLike(
						mt20id = mt20id,
						userId = userId
					)
				}
			} catch (e: RestException) {
				Log.d("create bookmarks", e.error)
			}
		}
	}

	fun deleteBookmark(mt20id: String, userId: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client
					.from("bookmarks")
					.delete {
						filter {
							eq("user_id", userId)
							eq("mt20id", mt20id)
						}
					}

				setIsLike(
					mt20id = mt20id,
					userId = userId
				)
			} catch (e: RestException) {
				Log.d("delete bookmarks", e.error)
			}
		}
	}
}
