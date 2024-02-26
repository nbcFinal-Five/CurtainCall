package com.nbc.curtaincall.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class RegisterInput(
	var email: String?,
	var password: String?,
	var passwordConfirm: String?,
	var name: String?,
	var gender: String?,
	var age: String?
)

class RegisterViewMode : ViewModel() {
	private var _input = MutableLiveData(
		RegisterInput(
			email = null,
			password = null,
			passwordConfirm = null,
			name = null,
			gender = null,
			age = null
		)
	)
	val input: LiveData<RegisterInput>
		get() = _input

	fun updateEmail(newEmail: String) {
		_input.value?.email = newEmail
		_input.value = _input.value
	}

	fun updatePassword(newPassword: String) {
		_input.value?.password = newPassword
		_input.value = _input.value
	}

	fun updatePasswordConfirm(newPasswordConfirm: String) {
		_input.value?.passwordConfirm = newPasswordConfirm
		_input.value = _input.value
	}

	fun updateName(newName: String) {
		_input.value?.name = newName
		_input.value = _input.value
	}

	fun updateGender(newGender: String) {
		_input.value?.gender = newGender
		_input.value = _input.value
	}

	fun updateAge(newAge: String) {
		_input.value?.age = newAge
		_input.value = _input.value
	}
}