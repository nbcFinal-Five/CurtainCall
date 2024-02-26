package com.nbc.curtaincall.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentRegisterBinding
import com.nbc.curtaincall.ui.UserViewModel
import java.util.regex.Pattern

class RegisterFragment : Fragment() {
	private val registerViewModel by lazy { ViewModelProvider(this)[RegisterViewModel::class.java] }
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private lateinit var binding: FragmentRegisterBinding
	private lateinit var btnGenders: List<AppCompatButton>
	private lateinit var btnAges: List<AppCompatButton>

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentRegisterBinding.inflate(inflater, container, false)
		btnGenders = listOf(binding.btnGenderMale, binding.btnGenderFemale)
		btnAges =
			listOf(binding.btnAge10, binding.btnAge20, binding.btnAge30, binding.btnAge40, binding.btnAge50, binding.btnAge60)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initHandle()
		initViewModel()
	}

	private fun initHandle() = with(binding) {
		etEmail.addTextChangedListener {
			registerViewModel.updateEmail(it.toString())
		}

		etPassword.addTextChangedListener {
			registerViewModel.updatePassword(it.toString())
		}

		etPasswordConfirm.addTextChangedListener {
			registerViewModel.updatePasswordConfirm(it.toString())
		}

		etNickname.addTextChangedListener {
			registerViewModel.updateName(it.toString())
		}

		btnGenders.forEach { button ->
			button.setOnClickListener {
				registerViewModel.updateGender(button.text.toString())
			}
		}

		btnAges.forEach { button ->
			button.setOnClickListener {
				registerViewModel.updateAge(button.text.toString())
			}
		}

		btnRegister.setOnClickListener {
			val inputs = registerViewModel.input.value

			userViewModel.signUp(
				inputEmail = inputs?.email!!,
				inputPassword = inputs.password!!,
				name = inputs.name!!,
				gender = inputs.gender!!,
				age = inputs.age!!
			)
		}
	}

	private fun initViewModel() {
		registerViewModel.input.observe(viewLifecycleOwner) { inputs ->
			// change ui when button clicked
			btnGenders.forEach { button ->
				if (button.text == inputs.gender) {
					button.setBackgroundResource(R.drawable.button_gradient)
					button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
				} else {
					button.setBackgroundResource(R.color.component_color)
					button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
				}
			}

			btnAges.forEach { button ->
				if (button.text == inputs.age) {
					button.setBackgroundResource(R.drawable.button_gradient)
					button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
				} else {
					button.setBackgroundResource(R.color.component_color)
					button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
				}
			}

			// check validation
			binding.tvEmailWarning.visibility = if (isValidEmail(inputs.email)) View.INVISIBLE else View.VISIBLE
			binding.tvEmailWarning.text = if (isValidEmail(inputs.email)) "" else getText(R.string.email_warning)

			binding.tvPasswordWarning.visibility = if (isValidPassword(inputs.password)) View.INVISIBLE else View.VISIBLE
			binding.tvPasswordConfirmWarning.visibility =
				if (isValidPasswordConfirm(inputs.password, inputs.passwordConfirm)) View.INVISIBLE else View.VISIBLE
			binding.tvNicknameWarning.visibility = if (isValidName(inputs.name)) View.INVISIBLE else View.VISIBLE

			// check total validation
			if (
				isValidEmail(inputs.email) &&
				isValidPassword(inputs.password) &&
				isValidPasswordConfirm(inputs.password, inputs.passwordConfirm) &&
				isValidName(inputs.name) &&
				!inputs.gender.isNullOrEmpty() &&
				!inputs.age.isNullOrEmpty()
			) {
				with(binding.btnRegister) {
					setBackgroundResource(R.drawable.button_gradient)
					setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
					isClickable = true
				}
			} else {
				with(binding.btnRegister) {
					setBackgroundResource(R.color.component_color)
					setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
					isClickable = false
				}
			}
		}

		userViewModel.isSignUpLoading.observe(viewLifecycleOwner) { isSignupLoading ->
			binding.btnRegister.isClickable = !isSignupLoading
			binding.btnRegister.setBackgroundResource(if (isSignupLoading) R.color.component_color else R.drawable.button_gradient)
		}

		userViewModel.signUpResult.observe(viewLifecycleOwner) { signupResult ->
			if (signupResult == null) {
				return@observe
			}

			binding.tvEmailWarning.visibility = if (signupResult) View.INVISIBLE else View.VISIBLE
			binding.tvEmailWarning.text = if (signupResult) "" else getText(R.string.email_duplicate)

			if (signupResult) {
				requireActivity().finish()
			} else {
				binding.etEmail.requestFocus()
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

	private fun isValidPasswordConfirm(password: String?, passwordConfirm: String?): Boolean {
		return password == passwordConfirm
	}

	private fun isValidName(name: String?): Boolean {
		return if (name == null) false else name.length >= 2
	}
}