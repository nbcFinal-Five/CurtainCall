package com.nbc.curtaincall.ui.main

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private var backPressedTime: Long = 0L


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val navView: BottomNavigationView = binding.navView

		val navController = findNavController(R.id.nav_host_fragment_activity_main)
		navView.setupWithNavController(navController)


		if(!isNetworkAvailable()){
			showNetworkDialog()
		}
	}

	// 네트워크 연결 상태 확인
	private fun isNetworkAvailable(): Boolean {
		val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val network = connectivityManager.activeNetwork
		val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
		return networkCapabilities != null &&
				(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
						networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
	}

	// 네트워크 미연결 시 다이얼로그 창
	private fun showNetworkDialog() {
		val builder = AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
		builder.setTitle(R.string.network_check_necessary)
			.setMessage(R.string.network_check_necessary_message)
			.setCancelable(false)
			.setPositiveButton(R.string.network_check) {
			 	dialog, _ ->
				dialog.dismiss()
				startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
			}
		val dialog = builder.create()
		dialog.show()
	}

	override fun onBackPressed() {
		val navView: BottomNavigationView = binding.navView

		// 현재 선택된 아이템이 가장 왼쪽의 아이템인지 확인
		if (navView.selectedItemId == R.id.navigation_home) {
			// 뒤로가기 버튼을 눌렀을 때 동작
			if (System.currentTimeMillis() - backPressedTime <= 2500) {
				super.onBackPressed()
				finish()
			} else {
				backPressedTime = System.currentTimeMillis()
				Toast.makeText(this, R.string.app_end_check, Toast.LENGTH_SHORT).show()
			}
		} else {
			super.onBackPressed()
		}
	}

}