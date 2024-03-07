package com.nbc.shownect.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nbc.shownect.R
import com.nbc.shownect.databinding.ActivityMainBinding
import com.nbc.shownect.supabase.Supabase
import io.github.jan.supabase.SupabaseClient

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private var backPressdTime: Long = 0L


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val navView: BottomNavigationView = binding.navView

		val navController = findNavController(R.id.nav_host_fragment_activity_main)
		navView.setupWithNavController(navController)

		initSupabase()
	}

	private fun initSupabase(): SupabaseClient {
		return Supabase.client
	}

	override fun onBackPressed() {
		val navView: BottomNavigationView = binding.navView

		// 현재 선택된 아이템이 가장 왼쪽의 아이템인지 확인
		if (navView.selectedItemId == R.id.navigation_home) {
			// 뒤로가기 버튼을 눌렀을 때 동작
			if (System.currentTimeMillis() - backPressdTime <= 2000) {
				super.onBackPressed()
				finish()
			} else {
				backPressdTime = System.currentTimeMillis()
				Toast.makeText(this, R.string.app_end_check, Toast.LENGTH_SHORT).show()
			}
		} else {
			super.onBackPressed()
		}
	}

}