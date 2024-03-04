package com.nbc.curtaincall.ui.more

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.curtaincall.supabase.Supabase
import com.nbc.curtaincall.supabase.model.GetReviewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoreViewModel : ViewModel() {
	companion object {
		const val LIMIT: Long = 4
	}

	private val _reviewPage = MutableLiveData<Int>(0)
	val reviewPage: LiveData<Int>
		get() = _reviewPage

	private val _reviewLoading = MutableLiveData<Boolean>(false)
	val reviewLoading: LiveData<Boolean>
		get() = _reviewLoading

	private val _reviewIsEnd = MutableLiveData<Boolean>(false)
	val reviewIsEnd: LiveData<Boolean>
		get() = _reviewIsEnd


	private val _reviewList = MutableLiveData<List<GetReviewModel>>(emptyList())
	val reviewList: LiveData<List<GetReviewModel>>
		get() = _reviewList

	fun loadMoreReviews(userId: String) {
		if (reviewLoading.value != true && _reviewIsEnd.value == false) {
			_reviewLoading.value = true

			CoroutineScope(Dispatchers.IO).launch {
				val skip = LIMIT * (_reviewPage.value ?: 0)

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
								eq(column = "user_id", value = userId)
							}
							order(column = "created_at", order = Order.DESCENDING)
							limit(count = LIMIT)
						}
						.decodeList<GetReviewModel>()

					withContext(Dispatchers.Main) {
						_reviewList.value = _reviewList.value?.plus(reviews)

						if (reviews.size < LIMIT) {
							_reviewIsEnd.value = true
						}
					}
				} catch (e: RestException) {
					Log.d("more", e.error)

				} finally {
					withContext(Dispatchers.Main) {
						if (_reviewIsEnd.value != true) {
							_reviewPage.value = (_reviewPage.value ?: 0) + 1
						}

						_reviewLoading.value = false
					}
				}
			}
		}
	}
}