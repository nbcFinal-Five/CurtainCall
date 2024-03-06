package com.nbc.curtaincall.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.curtaincall.BuildConfig
import com.nbc.curtaincall.supabase.Supabase
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class UserViewModel : ViewModel() {
	private var _userInfo = MutableLiveData<UserInfo?>(Supabase.client.auth.currentUserOrNull())
	val userInfo: LiveData<UserInfo?> = _userInfo

	// sign in
	private val _isSignInLoading = MutableLiveData(false)
	val isSignInLoading: LiveData<Boolean> get() = _isSignInLoading

	private val _signInResult = MutableLiveData<Boolean?>(null)
	val signInResult: LiveData<Boolean?> get() = _signInResult

	// sign out
	private val _isSignOutLoading = MutableLiveData(false)
	val isSignOutLoading: LiveData<Boolean> get() = _isSignOutLoading

	// sign up
	private val _isSignUpLoading = MutableLiveData(false)
	val isSignUpLoading: LiveData<Boolean> get() = _isSignUpLoading

	private val _signUpResult = MutableLiveData<Boolean?>(null)
	val signUpResult: LiveData<Boolean?> get() = _signUpResult

	private var _signUpErrorMessage = MutableLiveData<String?>(null)
	val signUpErrorMessage: LiveData<String?> = _signUpErrorMessage

	// quit
	private var _isQuitLoading = MutableLiveData(false)
	val isQuitLoading: LiveData<Boolean>
		get() = _isQuitLoading


	fun signIn(inputEmail: String, inputPassword: String) {
		_isSignInLoading.value = true

		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client.auth.signInWith(Email) {
					email = inputEmail
					password = inputPassword
				}

				withContext(Dispatchers.Main) {
					_userInfo.value = Supabase.client.auth.currentUserOrNull()
					_signInResult.value = true
				}
			} catch (e: RestException) {
				Log.d("sign in", e.error)
				withContext(Dispatchers.Main) {
					_signInResult.value = false
				}
			} finally {
				withContext(Dispatchers.Main) {
					_isSignInLoading.value = false
				}
			}
		}
	}

	fun signOut(onSuccess: () -> Unit) {
		_isSignOutLoading.value = true
		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client.auth.signOut()
			} catch (e: RestException) {
				Log.d("sign out", e.error)
			} finally {
				Supabase.client.auth.clearSession()
				withContext(Dispatchers.Main) {
					_userInfo.value = null
					_signInResult.value = null
					_signUpResult.value = null
					_signUpErrorMessage.value = null
					_isSignOutLoading.value = false

					onSuccess()
				}
			}
		}
	}

	fun signUp(
		inputEmail: String,
		inputPassword: String,
		name: String,
		gender: String,
		age: String,
	) {
		_isSignUpLoading.value = true

		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client.auth.signUpWith(Email) {
					email = inputEmail
					password = inputPassword
					data = buildJsonObject {
						put("name", name)
						put("gender", gender)
						put("age", age)
					}
				}

				withContext(Dispatchers.Main) {
					_userInfo.value = Supabase.client.auth.currentUserOrNull()
					_signUpResult.value = true
					_signUpErrorMessage.value = null
				}
			} catch (e: RestException) {
				Log.d("sign up", e.error)
				withContext(Dispatchers.Main) {
					_signUpResult.value = false
					_signUpErrorMessage.value = e.error
				}
			} finally {
				withContext(Dispatchers.Main) {
					_isSignUpLoading.value = false
				}
			}
		}
	}

	fun setUser() {
		_userInfo.value = Supabase.client.auth.currentUserOrNull()
	}

	fun quitUser(onSuccess: () -> Unit) {
		val user = Supabase.client.auth.currentUserOrNull() ?: return

		_isQuitLoading.value = true

		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client.postgrest.rpc(
					function = "delete_user",
					parameters = buildJsonObject {
						put("arg_user_id", user.id)
					}
				)

				val supabaseAdmin = createSupabaseClient(
					supabaseUrl = "https://cbwvdfwzjnbkzfmczhoo.supabase.co",
					supabaseKey = BuildConfig.SUPABASE_KEY
				) {
					install(Auth) {
						autoLoadFromStorage = false
						alwaysAutoRefresh = false
					}
				}
				supabaseAdmin.auth.importAuthToken(BuildConfig.SUPABASE_SERVICE_ROLE)

				supabaseAdmin.auth.admin.deleteUser(user.id)

				Supabase.client.auth.clearSession()
				withContext(Dispatchers.Main) {
					_userInfo.value = null
					_signInResult.value = null
					_signUpResult.value = null
					_signUpErrorMessage.value = null
					onSuccess()
				}
			} catch (e: RestException) {
				Log.d("quit", e.error)
			} finally {
				withContext(Dispatchers.Main) {
					_isQuitLoading.value = false
				}
			}
		}
	}
}