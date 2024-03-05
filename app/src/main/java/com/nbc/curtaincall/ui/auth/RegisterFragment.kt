package com.nbc.curtaincall.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.FragmentRegisterBinding
import com.nbc.curtaincall.supabase.Supabase
import com.nbc.curtaincall.ui.UserViewModel
import org.json.JSONException
import org.json.JSONObject
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
			val input = registerViewModel.input.value

			if (isValidInput(input!!)) {
				userViewModel.signUp(
					inputEmail = input?.email!!,
					inputPassword = input.password!!,
					name = input.name!!,
					gender = input.gender!!,
					age = input.age!!
				)
			}
		}
	}

	private fun initViewModel() {
		registerViewModel.input.observe(viewLifecycleOwner) { input ->
			// change ui when button clicked
			btnGenders.forEach { button ->
				if (button.text == input.gender) {
					button.setBackgroundResource(R.drawable.button_gradient)
					button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
				} else {
					button.setBackgroundResource(R.color.component_color)
					button.setTextColor(ContextCompat.getColor(requireContext(), R.color.component_background_color))
				}
			}

			btnAges.forEach { button ->
				if (button.text == input.age) {
					button.setBackgroundResource(R.drawable.button_gradient)
					button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
				} else {
					button.setBackgroundResource(R.color.component_color)
					button.setTextColor(ContextCompat.getColor(requireContext(), R.color.component_background_color))
				}
			}

			// check validation
			binding.tvEmailWarning.visibility = if (isValidEmail(input.email)) View.INVISIBLE else View.VISIBLE
			binding.tvEmailWarning.text = if (isValidEmail(input.email)) "" else getText(R.string.email_warning)

			binding.tvPasswordWarning.visibility = if (isValidPassword(input.password)) View.INVISIBLE else View.VISIBLE
			binding.tvPasswordConfirmWarning.visibility =
				if (isValidPasswordConfirm(input.password, input.passwordConfirm)) View.INVISIBLE else View.VISIBLE
			binding.tvNicknameWarning.visibility = if (isValidName(input.name)) View.INVISIBLE else View.VISIBLE

			val isWarningGenderAge = input.gender == null || input.age == null
			binding.tvGenderAgeWarning.visibility = if (!isWarningGenderAge) View.INVISIBLE else View.VISIBLE

			// check total validation
			if (isValidInput(input)) {
				with(binding.btnRegister) {
					setBackgroundResource(R.drawable.button_gradient)
					setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
					isClickable = true
				}
			} else {
				with(binding.btnRegister) {
					setBackgroundResource(R.color.component_color)
					setTextColor(ContextCompat.getColor(requireContext(), R.color.component_background_color))
					isClickable = false
				}
			}
		}

		userViewModel.isSignUpLoading.observe(viewLifecycleOwner) { isSignupLoading ->
			binding.btnRegister.isClickable = !isSignupLoading

			if (isValidInput(registerViewModel.input.value!!)) {
				binding.btnRegister.setBackgroundResource(
					if (isSignupLoading) R.color.component_color else R.drawable.button_gradient
				)
				binding.btnRegister.setTextColor(
					ContextCompat.getColor(
						requireContext(),
						if (isSignupLoading) R.color.component_background_color else R.color.white
					)
				)
			} else {
				binding.btnRegister.setBackgroundResource(R.color.component_color)
				binding.btnRegister.setTextColor(ContextCompat.getColor(requireContext(), R.color.component_background_color))
			}
		}

		userViewModel.signUpResult.observe(viewLifecycleOwner) { signupResult ->
			if (signupResult == null) {
				return@observe
			}

			if (signupResult) {
				requireActivity().finish()
			}
		}

		userViewModel.signUpErrorMessage.observe(viewLifecycleOwner) { signUpErrorMessage ->
			val errorCode = Supabase.getCodeFromSupabaseError(signUpErrorMessage)

			when (errorCode) {
				23505 -> {
					binding.tvNicknameWarning.visibility = View.VISIBLE
					binding.tvNicknameWarning.text = getText(R.string.nickname_duplicate)
					showKeyboard(binding.etNickname)
				}

				null -> {
					if (signUpErrorMessage == "User already registered") {
						binding.tvEmailWarning.visibility = View.VISIBLE
						binding.tvEmailWarning.text = getText(R.string.email_duplicate)
						showKeyboard(binding.etEmail)
					}
				}
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

	private fun isValidInput(input: RegisterInput): Boolean {
		return isValidEmail(input.email) &&
						isValidPassword(input.password) &&
						isValidPasswordConfirm(input.password, input.passwordConfirm) &&
						isValidName(input.name) &&
						!input.gender.isNullOrEmpty() &&
						!input.age.isNullOrEmpty()
	}

	private fun showKeyboard(editText: EditText) {
		editText.requestFocus()
		val imm = requireActivity().getSystemService(InputMethodManager::class.java)
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
	}
}