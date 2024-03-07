package com.nbc.shownect.ui.detail_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.shownect.supabase.Supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {
	companion object {
		const val EXPECTATION = "EXPECTATION"
		const val REVIEW = "REVIEW"
	}

	private var _totalExpectationCount: MutableLiveData<Int> = MutableLiveData(0)
	val totalExpectationCount: LiveData<Int>
		get() = _totalExpectationCount

	private var _mode: MutableLiveData<String> = MutableLiveData(EXPECTATION)
	val mode: LiveData<String>
		get() = _mode

	fun setMode(mode: String) {
		_mode.value = mode
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
}