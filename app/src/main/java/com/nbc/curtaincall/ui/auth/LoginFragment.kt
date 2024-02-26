package com.nbc.curtaincall.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentLoginBinding

class LoginFragment(private val supportFragmentManager: FragmentManager) : Fragment() {
	private lateinit var binding: FragmentLoginBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentLoginBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initHandle()
	}

	private fun initHandle() {
		binding.tvRegister.setOnClickListener {
			supportFragmentManager.commit {
				replace(R.id.fragment_auth, RegisterFragment())
				setReorderingAllowed(true)
			}
		}
	}
}