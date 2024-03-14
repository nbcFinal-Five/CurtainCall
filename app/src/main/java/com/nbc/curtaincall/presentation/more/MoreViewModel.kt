package com.nbc.curtaincall.ui.more

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

class MoreViewModel : ViewModel() {
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

	fun loadMoreReviews(userId: String) {
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
								eq(column = "user_id", value = userId)
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


	// bookmarks
	private val _bookmarksPage = MutableLiveData<Int>(0)
	val bookmarksPage: LiveData<Int>
		get() = _bookmarksPage

	private val _bookmarksLoading = MutableLiveData<Boolean>(false)
	val bookmarksLoading: LiveData<Boolean>
		get() = _bookmarksLoading

	private val _bookmarksList = MutableLiveData<List<GetBookmarkModel>>(emptyList())
	val bookmarksList: LiveData<List<GetBookmarkModel>>
		get() = _bookmarksList

	fun loadMoreBookmarks(userId: String) {
		if (_bookmarksLoading.value != true && _isEnd.value == false) {
			_bookmarksLoading.value = true

			CoroutineScope(Dispatchers.IO).launch {
				try {
					val columns = Columns.raw(
						"""
					*
				""".trimIndent()
					)

					val bookmarks = Supabase.client
						.from("bookmarks")
						.select(columns) {
							filter {
								eq(column = "user_id", value = userId)
							}
							order(column = "created_at", order = Order.DESCENDING)
							limit(count = LIMIT)
							range((_bookmarksPage.value!!) * LIMIT, (_bookmarksPage.value!! + 1) * LIMIT - 1)
						}
						.decodeList<GetBookmarkModel>()

					withContext(Dispatchers.Main) {
						_bookmarksList.value = _bookmarksList.value?.plus(bookmarks)

						if (bookmarks.size < LIMIT) {
							_isEnd.value = true
						}
					}
				} catch (e: RestException) {
					Log.d("more", e.error)

				} finally {
					withContext(Dispatchers.Main) {
						if (_isEnd.value != true) {
							_bookmarksPage.value = (_bookmarksPage.value ?: 0) + 1
						}

						_bookmarksLoading.value = false
					}
				}
			}
		}
	}
}