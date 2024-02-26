package com.nbc.curtaincall.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {


	private var _binding: FragmentMyPageBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		when (result.resultCode) {

		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val notificationsViewModel =
			ViewModelProvider(this).get(MyPageViewModel::class.java)

		_binding = FragmentMyPageBinding.inflate(inflater, container, false)
		val root: View = binding.root


		notificationsViewModel.text.observe(viewLifecycleOwner) {

		}
		return root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}