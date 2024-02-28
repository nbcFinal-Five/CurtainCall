package com.nbc.curtaincall.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.curtaincall.supabase.Supabase
import com.nbc.curtaincall.supabase.model.GetBookmarkModel
import com.nbc.curtaincall.supabase.model.GetReviewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPageViewModel : ViewModel() {
	// reviews
	private val _isReviewLoading = MutableLiveData(false)
	val isReviewLoading: LiveData<Boolean>
		get() = _isReviewLoading

	private val _reviews = MutableLiveData<List<GetReviewModel>?>(null)
	val reviews: LiveData<List<GetReviewModel>?>
		get() = _reviews

	// bookmarks
	private val _isBookmarkLoading = MutableLiveData(false)
	val isBookmarkLoading: LiveData<Boolean>
		get() = _isBookmarkLoading

	private val _bookmarks = MutableLiveData<List<GetBookmarkModel>?>(null)
	val bookmarks: LiveData<List<GetBookmarkModel>?>
		get() = _bookmarks

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
							eq(column = "user_id", value = userId)
						}
						order(column = "created_at", order = Order.DESCENDING)
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

	fun setBookmarks(userId: String) {
		_isBookmarkLoading.value = true

		CoroutineScope(Dispatchers.IO).launch {
			try {
				val columns = Columns.raw(
					"""
					*
				""".trimIndent()
				)

				val reviews = Supabase.client
					.from("bookmarks")
					.select(columns) {
						filter {
							eq(column = "user_id", value = userId)
						}
						order(column = "created_at", order = Order.DESCENDING)
					}
					.decodeList<GetBookmarkModel>()

				withContext(Dispatchers.Main) {
					_bookmarks.value = reviews
				}
			} catch (e: RestException) {
				Log.d("my reviews", e.error)
			} finally {
				withContext(Dispatchers.Main) {
					_isBookmarkLoading.value = false
				}
			}
		}
	}


	fun clear() {
		_reviews.value = null
		_bookmarks.value = null
	}
}