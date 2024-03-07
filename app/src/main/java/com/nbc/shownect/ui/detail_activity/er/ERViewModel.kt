package com.nbc.shownect.ui.detail_activity.er

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ERViewModel : ViewModel() {
	companion object {
		const val EXPECTATION = "EXPECTATION"
		const val REVIEW = "REVIEW"
	}

	private var _mode: MutableLiveData<String> = MutableLiveData(EXPECTATION)
	val mode: LiveData<String>
		get() = _mode


	fun setMode(mode: String) {
		_mode.value = mode
	}
}