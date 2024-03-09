package com.nbc.shownect.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class LoginInput(
	var email: String?,
	var password: String?,
)

class LoginViewModel : ViewModel() {
	private var _input = MutableLiveData(
		LoginInput(
			email = null,
			password = null
		)
	)

	val input: LiveData<LoginInput>
		get() = _input

	fun updateEmail(newEmail: String) {
		_input.value?.email = newEmail
		_input.value = _input.value
	}

	fun updatePassword(newPassword: String) {
		_input.value?.password = newPassword
		_input.value = _input.value
	}
}