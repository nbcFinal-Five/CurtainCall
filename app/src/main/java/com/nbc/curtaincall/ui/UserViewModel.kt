package com.nbc.curtaincall.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nbc.curtaincall.BuildConfig
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
	private val supabaseClient = createSupabaseClient(
		supabaseUrl = "https://cbwvdfwzjnbkzfmczhoo.supabase.co",
		supabaseKey = BuildConfig.SUPABASE_KEY
	) {
		install(Postgrest)
		install(Auth)
	}

	private var _userInfo = MutableLiveData<UserInfo?>(supabaseClient.auth.currentUserOrNull())
	val userInfo: LiveData<UserInfo?> = _userInfo

	fun signIn(inputEmail: String, inputPassword: String) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				supabaseClient.auth.signInWith(Email) {
					email = inputEmail
					password = inputPassword
				}

				withContext(Dispatchers.Main) {
					_userInfo.value = supabaseClient.auth.currentUserOrNull()
				}
			} catch (e: Exception) {
				Log.d("sign in", e.toString())
			}
		}
	}

	fun signOut() {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				supabaseClient.auth.signOut()
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
				val signUpResult = supabaseClient.auth.signUpWith(Email) {
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