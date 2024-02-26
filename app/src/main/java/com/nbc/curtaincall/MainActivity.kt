package com.nbc.curtaincall

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.nbc.curtaincall.databinding.ActivityMainBinding
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val navView: BottomNavigationView = binding.navView

		val navController = findNavController(R.id.nav_host_fragment_activity_main)
		val appBarConfiguration = AppBarConfiguration(
			setOf(
				R.id.navigation_home, R.id.navigation_search, R.id.navigation_my_page
			)
		)
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView.setupWithNavController(navController)

		initSupabase()
	}

	private fun initSupabase(): SupabaseClient {
		return createSupabaseClient(
			supabaseUrl = "https://cbwvdfwzjnbkzfmczhoo.supabase.co",
			supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNid3ZkZnd6am5ia3pmbWN6aG9vIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDg5MDc4MDAsImV4cCI6MjAyNDQ4MzgwMH0.K2WblsQ58eZbb6M2N-ULiGjrmYFTkWlKjYngnO3L-g0"
		) {
			install(Postgrest)
			install(Auth)
		}
	}
}