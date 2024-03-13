package com.nbc.shownect.presentation.detail_activity.er.more

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.shownect.supabase.Supabase
import com.nbc.shownect.supabase.model.GetExpectationModel
import com.nbc.shownect.supabase.model.GetReviewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ErMoreViewModel : ViewModel() {
	companion object {
		const val LIMIT: Long = 18
	}

	//	reviews
	private val _reviewPage = MutableLiveData<Int>(0)
	val reviewPage: LiveData<Int>
		get() = _reviewPage

	private val _reviewLoading = MutableLiveData<Boolean>(false)
	val reviewLoading: LiveData<Boolean>
		get() = _reviewLoading

	private val _isEnd = MutableLiveData<Boolean>(false)
	val isEnd: LiveData<Boolean>
		get() = _isEnd


	private val _reviewList = MutableLiveData<List<GetReviewModel>>(emptyList())
	val reviewList: LiveData<List<GetReviewModel>>
		get() = _reviewList

	fun loadMoreReviews(mt20id: String) {
		if (_reviewLoading.value != true && _isEnd.value == false) {
			_reviewLoading.value = true

			CoroutineScope(Dispatchers.IO).launch {
				try {
					val columns = Columns.raw(
						"""
					*, profile:profiles(*)
				""".trimIndent()
					)

					val reviews = Supabase.client
						.from("reviews")
						.select(columns) {
							filter {
								eq(column = "mt20id", value = mt20id)
							}
							order(column = "created_at", order = Order.DESCENDING)
							limit(count = LIMIT)
							range((_reviewPage.value!!) * LIMIT, (_reviewPage.value!! + 1) * LIMIT - 1)
						}
						.decodeList<GetReviewModel>()

					withContext(Dispatchers.Main) {
						_reviewList.value = _reviewList.value?.plus(reviews)

						if (reviews.size < LIMIT) {
							_isEnd.value = true
						}
					}
				} catch (e: RestException) {
					Log.d("more", e.error)

				} finally {
					withContext(Dispatchers.Main) {
						if (_isEnd.value != true) {
							_reviewPage.value = (_reviewPage.value ?: 0) + 1
						}

						_reviewLoading.value = false
					}
				}
			}
		}
	}


	// expectations
	private val _expectationsPage = MutableLiveData<Int>(0)
	val expectationsPage: LiveData<Int>
		get() = _expectationsPage

	private val _expectationsLoading = MutableLiveData<Boolean>(false)
	val expectationsLoading: LiveData<Boolean>
		get() = _expectationsLoading

	private val _expectationsList = MutableLiveData<List<GetExpectationModel>>(emptyList())
	val expectationsList: LiveData<List<GetExpectationModel>>
		get() = _expectationsList

	fun loadMoreExpectations(mt20id: String) {
		if (_expectationsLoading.value != true && _isEnd.value == false) {
			_expectationsLoading.value = true

			CoroutineScope(Dispatchers.IO).launch {
				try {
					val columns = Columns.raw(
						"""
					*, profile:profiles(*)
				""".trimIndent()
					)

					val expectations = Supabase.client
						.from("expectations")
						.select(columns) {
							filter {
								eq(column = "mt20id", value = mt20id)
							}
							order(column = "created_at", order = Order.DESCENDING)
							limit(count = LIMIT)
							range((_expectationsPage.value!!) * LIMIT, (_expectationsPage.value!! + 1) * LIMIT - 1)
						}
						.decodeList<GetExpectationModel>()

					withContext(Dispatchers.Main) {
						_expectationsList.value = _expectationsList.value?.plus(expectations)

						if (expectations.size < LIMIT) {
							_isEnd.value = true
						}
					}
				} catch (e: RestException) {
					Log.d("more", e.error)

				} finally {
					withContext(Dispatchers.Main) {
						if (_isEnd.value != true) {
							_expectationsPage.value = (_expectationsPage.value ?: 0) + 1
						}

						_expectationsLoading.value = false
					}
				}
			}
		}
	}
}