package com.nbc.shownect.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentDialogQuitBinding
import com.nbc.shownect.ui.UserViewModel

class QuitDialogFragment : DialogFragment() {
	interface QuitDialogListener {
		fun onQuitConfirmed()
	}

	fun setListener(listener: QuitDialogListener) {
		this.listener = listener
	}

	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private lateinit var binding: FragmentDialogQuitBinding
	private var listener: QuitDialogListener? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentDialogQuitBinding.inflate(inflater, container, false)
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
			userViewModel.quitUser {
				listener?.onQuitConfirmed()
				dismiss()
			}
		}
	}

	private fun initViewModel() = with(binding) {
		userViewModel.isQuitLoading.observe(viewLifecycleOwner) {
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