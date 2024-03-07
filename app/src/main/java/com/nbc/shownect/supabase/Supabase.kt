package com.nbc.shownect.supabase

import com.nbc.shownect.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import org.json.JSONException
import org.json.JSONObject

object Supabase {
	val client = createSupabaseClient(
		supabaseUrl = "https://cbwvdfwzjnbkzfmczhoo.supabase.co",
		supabaseKey = BuildConfig.SUPABASE_KEY
	) {
		install(Postgrest)
		install(Auth)
	}

	fun getCodeFromSupabaseError(string: String?): Int? {
		if (string == null) return null

		try {
			val code = JSONObject(string).getInt("code")
			return code
		} catch (e: JSONException) {
			return null
		}
	}
}