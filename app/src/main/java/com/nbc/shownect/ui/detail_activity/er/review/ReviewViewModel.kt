package com.nbc.shownect.ui.detail_activity.er.review

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.shownect.supabase.Supabase
import com.nbc.shownect.supabase.model.GetReviewModel
import com.nbc.shownect.supabase.model.PostReviewModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewViewModel : ViewModel() {
	private var _point: MutableLiveData<Int?> = MutableLiveData(null)
	val point: LiveData<Int?>
		get() = _point

	private var _comment: MutableLiveData<String?> = MutableLiveData("")
	val comment: LiveData<String?>
		get() = _comment

	private val _isCreateReviewLoading: MutableLiveData<Boolean> = MutableLiveData(false)
	val isCreateReviewLoading: LiveData<Boolean>
		get() = _isCreateReviewLoading

	private var _totalCount: MutableLiveData<Int> = MutableLiveData(0)
	val totalCount: LiveData<Int>
		get() = _totalCount

	private var _isReadReviewLoading: MutableLiveData<Boolean> = MutableLiveData(false)
	val isReadReviewLoading: LiveData<Boolean>
		get() = _isReadReviewLoading

	private var _comments: MutableLiveData<List<GetReviewModel>> = MutableLiveData(emptyList())
	val comments: LiveData<List<GetReviewModel>>
		get() = _comments


	fun setPoint(newPoint: Int?) {
		_point.value = newPoint
	}

	fun setComment(inputComment: String?) {
		_comment.value = inputComment
	}

	fun createReview(model: PostReviewModel, context: Context, errorMessage: String) {
		_isCreateReviewLoading.value = true

		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client
					.from("reviews")
					.insert<PostReviewModel>(model)

				withContext(Dispatchers.Main) {
					setTotalCount(model.mt20id)
					setList(model.mt20id)
				}
			} catch (e: RestException) {
				Log.d("create review", e.error)

				withContext(Dispatchers.Main) {
					Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
				}
			} finally {
				withContext(Dispatchers.Main) {
					_isCreateReviewLoading.value = false
				}
			}
		}
	}

	fun setTotalCount(mt20id: String) {
		CoroutineScope(Dispatchers.IO).launch {
			val count = Supabase.client
				.from("reviews")
				.select {
					filter {
						eq(column = "mt20id", value = mt20id)
					}
					count(Count.EXACT)
				}.countOrNull()!!

			withContext(Dispatchers.Main) { _totalCount.value = count.toInt() }
		}
	}

	fun setList(mt20id: String) {
		_isReadReviewLoading.value = true

		CoroutineScope(Dispatchers.IO).launch {
			try {
				val columns = Columns.raw(
					"""
					*, profile:profiles(*)
				""".trimIndent()
				)

				val list = Supabase.client
					.from("reviews")
					.select(columns) {
						filter {
							eq(column = "mt20id", value = mt20id)
						}

						order(column = "created_at", order = Order.DESCENDING)
						range(0, 3)
					}
					.decodeList<GetReviewModel>()

				withContext(Dispatchers.Main) { _comments.value = list }
			} catch (e: RestException) {
				Log.d("read Reviews", e.error)
			} finally {
				withContext(Dispatchers.Main) { _isReadReviewLoading.value = false }
			}
		}
	}
}