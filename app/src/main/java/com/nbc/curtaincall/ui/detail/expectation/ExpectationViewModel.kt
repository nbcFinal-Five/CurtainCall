package com.nbc.curtaincall.ui.detail.expectation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExpectationViewModel : ViewModel() {
	private var _isExpect: MutableLiveData<Boolean?> = MutableLiveData(null)
	val isExpect: LiveData<Boolean?>
		get() = _isExpect

	private var _comment: MutableLiveData<String?> = MutableLiveData("")
	val comment: LiveData<String?>
		get() = _comment

	fun setIsExpect(expect: Boolean?) {
		_isExpect.value = expect
	}

	fun setComment(inputComment: String?) {
		_comment.value = inputComment
	}
}