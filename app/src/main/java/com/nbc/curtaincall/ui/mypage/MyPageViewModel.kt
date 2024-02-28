package com.nbc.curtaincall.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.curtaincall.supabase.Supabase
import com.nbc.curtaincall.supabase.model.GetReviewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPageViewModel : ViewModel() {
	private val _isReviewLoading = MutableLiveData(false)
	val isReviewLoading: LiveData<Boolean>
		get() = _isReviewLoading

	private val _reviews = MutableLiveData<List<GetReviewModel>?>(null)
	val reviews: LiveData<List<GetReviewModel>?>
		get() = _reviews

	fun setReview(userId: String) {
		_isReviewLoading.value = true

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
							eq("user_id", userId)
						}
					}
					.decodeList<GetReviewModel>()
				
				withContext(Dispatchers.Main) {
					_reviews.value = reviews
				}
			} catch (e: RestException) {
				Log.d("my reviews", e.error)
			} finally {
				withContext(Dispatchers.Main) {
					_isReviewLoading.value = false
				}
			}
		}
	}

	fun clearReviews() {
		_reviews.value = null
	}
}