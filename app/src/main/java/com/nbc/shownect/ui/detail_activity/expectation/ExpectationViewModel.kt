package com.nbc.shownect.ui.detail_activity.expectation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.shownect.supabase.Supabase
import com.nbc.shownect.supabase.model.GetExpectationModel
import com.nbc.shownect.supabase.model.PostExpectationModel
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpectationViewModel : ViewModel() {
	private var _isExpect: MutableLiveData<Boolean?> = MutableLiveData(null)
	val isExpect: LiveData<Boolean?>
		get() = _isExpect

	private var _comment: MutableLiveData<String?> = MutableLiveData("")
	val comment: LiveData<String?>
		get() = _comment

	private val _isCreateExpectationLoading: MutableLiveData<Boolean> = MutableLiveData(false)
	val isCreateExpectationLoading: LiveData<Boolean>
		get() = _isCreateExpectationLoading

	private var _goodCount: MutableLiveData<Int> = MutableLiveData(0)
	val goodCount: LiveData<Int>
		get() = _goodCount

	private var _badCount: MutableLiveData<Int> = MutableLiveData(0)
	val badCount: LiveData<Int>
		get() = _badCount

	private var _totalCount: MutableLiveData<Int> = MutableLiveData(0)
	val totalCount: LiveData<Int>
		get() = _totalCount

	private var _isReadExpectationLoading: MutableLiveData<Boolean> = MutableLiveData(false)
	val isReadExpectationLoading: LiveData<Boolean>
		get() = _isReadExpectationLoading

	private var _comments: MutableLiveData<List<GetExpectationModel>> = MutableLiveData(emptyList())
	val comments: LiveData<List<GetExpectationModel>>
		get() = _comments


	fun setIsExpect(expect: Boolean?) {
		_isExpect.value = expect
	}

	fun setComment(inputComment: String?) {
		_comment.value = inputComment
	}

	fun createExpectation(model: PostExpectationModel, context: Context, errorMessage: String, onSuccess: () -> Unit) {
		_isCreateExpectationLoading.value = true

		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client
					.from("expectations")
					.insert<PostExpectationModel>(model)


				withContext(Dispatchers.Main) {
					setCount(model.mt20id)
					setList(model.mt20id)
				}
				onSuccess()
			} catch (e: RestException) {
				Log.d("create expectation", e.error)

				if (e.error == "duplicate key value violates unique constraint \"unique_mt20id_user_id\"") {
					withContext(Dispatchers.Main) {
						Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
					}
				}
			} finally {
				withContext(Dispatchers.Main) {
					_isCreateExpectationLoading.value = false
				}
			}
		}
	}

	fun setCount(mt20id: String) {
		CoroutineScope(Dispatchers.IO).launch {
			val count = Supabase.client
				.from("expectations")
				.select {
					filter {
						eq(column = "mt20id", value = mt20id)
						eq(column = "is_expect", value = true)
					}
					count(Count.EXACT)
				}.countOrNull()!!

			withContext(Dispatchers.Main) { _goodCount.value = count.toInt() }
		}

		CoroutineScope(Dispatchers.IO).launch {
			val count = Supabase.client
				.from("expectations")
				.select {
					filter {
						eq(column = "mt20id", value = mt20id)
						eq(column = "is_expect", value = false)
					}
					count(Count.EXACT)
				}.countOrNull()!!

			withContext(Dispatchers.Main) { _badCount.value = count.toInt() }
		}

		CoroutineScope(Dispatchers.IO).launch {
			val count = Supabase.client
				.from("expectations")
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
		_isReadExpectationLoading.value = true

		CoroutineScope(Dispatchers.IO).launch {
			try {
				val columns = Columns.raw(
					"""
					*, profile:profiles(*)
				""".trimIndent()
				)

				val list = Supabase.client
					.from("expectations")
					.select(columns) {
						filter {
							eq(column = "mt20id", value = mt20id)
						}

						order(column = "created_at", order = Order.DESCENDING)
						range(0, 3)
					}
					.decodeList<GetExpectationModel>()

				withContext(Dispatchers.Main) { _comments.value = list }
			} catch (e: RestException) {
				Log.d("read expectations", e.error)
			} finally {
				withContext(Dispatchers.Main) { _isReadExpectationLoading.value = false }
			}
		}
	}
}