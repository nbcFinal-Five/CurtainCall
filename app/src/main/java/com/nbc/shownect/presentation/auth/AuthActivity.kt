package com.nbc.shownect.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.nbc.shownect.R
import com.nbc.shownect.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
	private lateinit var binding: ActivityAuthBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAuthBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.btnAuthBack.setOnClickListener {
			finish()
		}

		setFragment(LoginFragment(supportFragmentManager))
	}

	private fun setFragment(frag: Fragment) {  //2번
		supportFragmentManager.commit {
			replace(R.id.fragment_auth, frag)
			setReorderingAllowed(true)
		}
	}
}