package com.nbc.curtaincall.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
	private lateinit var binding: ActivityAuthBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAuthBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setFragment(LoginFragment(supportFragmentManager))
	}

	private fun setFragment(frag: Fragment) {  //2번
		supportFragmentManager.commit {
			replace(R.id.fragment_auth, frag)
			setReorderingAllowed(true)
		}
	}
}