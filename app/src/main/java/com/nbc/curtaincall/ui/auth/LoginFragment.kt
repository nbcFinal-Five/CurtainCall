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
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentLoginBinding
import com.nbc.curtaincall.ui.UserViewModel

class LoginFragment(private val supportFragmentManager: FragmentManager) : Fragment() {
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

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
		initViewModel()
	}

	private fun initHandle() = with(binding) {
		tvRegister.setOnClickListener {
			supportFragmentManager.commit {
				replace(R.id.fragment_auth, RegisterFragment())
				setReorderingAllowed(true)
			}
		}

		btnSignIn.setOnClickListener {
			userViewModel.signIn(
				inputEmail = etInputEmail.text.toString(),
				inputPassword = etInputPassword.text.toString()
			)
		}
	}

	private fun initViewModel() {
		userViewModel.signInResult.observe(viewLifecycleOwner) { signInResult ->
			if (signInResult == null) {
				return@observe
			}

			if (signInResult) {
				requireActivity().finish()
			} else {
				// TODO 로그인 실패시
			}
		}

		userViewModel.isSignInLoading.observe(viewLifecycleOwner) { isSignInLoading ->
			// TODO 로그인 중일때?
		}
	}
}