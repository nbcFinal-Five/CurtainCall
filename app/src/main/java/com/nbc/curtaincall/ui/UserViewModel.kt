package com.nbc.curtaincall.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.curtaincall.BuildConfig
import com.nbc.curtaincall.Supabase
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class UserViewModel : ViewModel() {
	private var _userInfo = MutableLiveData<UserInfo?>(Supabase.client.auth.currentUserOrNull())
	val userInfo: LiveData<UserInfo?> = _userInfo

	fun signIn(inputEmail: String, inputPassword: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client.auth.signInWith(Email) {
					email = inputEmail
					password = inputPassword
				}

				withContext(Dispatchers.Main) {
					_userInfo.value = Supabase.client.auth.currentUserOrNull()
				}
			} catch (e: Exception) {
				Log.d("sign in", e.toString())
			}
		}
	}

	fun signOut() {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				Supabase.client.auth.signOut()
			} catch (e: Exception) {
				Log.d("sign out", e.toString())
			} finally {
				withContext(Dispatchers.Main) {
					_userInfo.value = null
				}
			}
		}
	}

	fun signUp(inputEmail: String, inputPassword: String, name: String, gender: String, age: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val signUpResult = Supabase.client.auth.signUpWith(Email) {
					email = inputEmail
					password = inputPassword
					data = buildJsonObject {
						put("name", name)
						put("gender", gender)
						put("age", age)
					}
				}
				//TODO
			} catch (e: Exception) {
				Log.d("sign up", e.toString())
			}
		}
	}
}