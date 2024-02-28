package com.nbc.curtaincall.supabase

import com.nbc.curtaincall.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object Supabase {
	val client = createSupabaseClient(
		supabaseUrl = "https://cbwvdfwzjnbkzfmczhoo.supabase.co",
		supabaseKey = BuildConfig.SUPABASE_KEY
	) {
		install(Postgrest)
		install(Auth)
	}
}