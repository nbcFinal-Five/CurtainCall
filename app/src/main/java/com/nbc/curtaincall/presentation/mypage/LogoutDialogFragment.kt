package com.nbc.curtaincall.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.R
import com.nbc.curtaincall.databinding.ModalLogoutBinding
import com.nbc.curtaincall.ui.UserViewModel


class LogoutDialogFragment : DialogFragment() {
	interface LogoutDialogListener {
		fun onLogoutConfirmed()
	}


	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private lateinit var binding: ModalLogoutBinding
	private var listener: LogoutDialogListener? = null

	fun setListener(listener: LogoutDialogListener) {
		this.listener = listener
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = ModalLogoutBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initHandler()
		initViewModel()
	}

	private fun initHandler() = with(binding) {
		btnCancel.setOnClickListener {
			dismiss()
		}

		btnOk.setOnClickListener {
			userViewModel.signOut {
				listener?.onLogoutConfirmed()
				dismiss()
			}
		}
	}

	private fun initViewModel() = with(binding) {
		userViewModel.isSignOutLoading.observe(viewLifecycleOwner) {
			btnCancel.isClickable = !it
			btnOk.isClickable = !it

			if (it) {
				btnOk.setBackgroundResource(R.color.component_background_color)
				btnOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.component_color))
			} else {
				btnOk.setBackgroundResource(R.color.component_color)
				btnOk.setTextColor(ContextCompat.getColor(requireContext(), R.color.component_background_color))
			}
		}
	}
}