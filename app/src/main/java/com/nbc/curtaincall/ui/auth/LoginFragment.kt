package com.nbc.curtaincall.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentLoginBinding
import com.nbc.curtaincall.ui.UserViewModel
import java.util.regex.Pattern

class LoginFragment(private val supportFragmentManager: FragmentManager) : Fragment() {
	private val loginViewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
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
			val input = loginViewModel.input.value

			if (isValidInput(input!!)) {
				userViewModel.signIn(
					inputEmail = etInputEmail.text.toString(),
					inputPassword = etInputPassword.text.toString()
				)
			}
		}

		etInputEmail.addTextChangedListener {
			loginViewModel.updateEmail(it.toString())
		}

		etInputPassword.addTextChangedListener {
			loginViewModel.updatePassword(it.toString())
		}
	}

	private fun initViewModel() {
		loginViewModel.input.observe(viewLifecycleOwner) { input ->
			binding.tvEmailWarning.visibility = if (isValidEmail(input.email)) View.INVISIBLE else View.VISIBLE
			binding.tvPasswordWarning.visibility = if (isValidPassword(input.password)) View.INVISIBLE else View.VISIBLE

			if (isValidInput(input)) {
				with(binding.btnSignIn) {
					setBackgroundResource(R.drawable.button_gradient)
					setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
					isClickable = true
				}
			} else {
				with(binding.btnSignIn) {
					setBackgroundResource(R.color.component_color)
					setTextColor(ContextCompat.getColor(requireContext(), R.color.component_background_color))
					isClickable = false
				}
			}
		}

		userViewModel.isSignInLoading.observe(viewLifecycleOwner) { isSignInLoading ->
			binding.btnSignIn.isClickable = !isSignInLoading

			if (isValidInput(loginViewModel.input.value!!)) {
				binding.btnSignIn.setBackgroundResource(
					if (isSignInLoading) R.color.component_color else R.drawable.button_gradient
				)
				binding.btnSignIn.setTextColor(
					ContextCompat.getColor(
						requireContext(),
						if (isSignInLoading) R.color.component_background_color else R.color.white
					)
				)
			} else {
				binding.btnSignIn.setBackgroundResource(R.color.component_color)
				binding.btnSignIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.component_background_color))
			}
		}

		userViewModel.signInResult.observe(viewLifecycleOwner) { signInResult ->
			if (signInResult == null) {
				return@observe
			}

			if (signInResult) {
				requireActivity().finish()
			} else {
				binding.tvFailSignIn.visibility = View.VISIBLE
			}
		}
	}

	private fun isValidEmail(email: String?): Boolean {
		val emailPattern = Pattern.compile(
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
							"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
		)
		val matcher = email?.let { emailPattern.matcher(it) }
		return matcher?.matches() ?: false
	}

	private fun isValidPassword(password: String?): Boolean {
		return if (password == null) false else password.length >= 6
	}

	private fun isValidInput(input: LoginInput): Boolean {
		return isValidEmail(input.email) &&
						isValidPassword(input.password)
	}
}