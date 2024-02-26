package com.nbc.curtaincall.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.Supabase
import com.nbc.curtaincall.databinding.FragmentMyPageBinding
import com.nbc.curtaincall.ui.UserViewModel
import io.github.jan.supabase.gotrue.auth

class MyPageFragment : Fragment() {
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private var _binding: FragmentMyPageBinding? = null
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

		return root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initViewModel()
		initHandle()
	}

	private fun initHandle() = with(binding) {
		btnOpenAuthActivity.setOnClickListener {
			
		}
	}

	private fun initViewModel() {
		userViewModel.userInfo.observe(viewLifecycleOwner) {
			val currentSession = Supabase.client.auth.currentSessionOrNull()

			if (currentSession == null) {
				binding.clUserInfo.visibility = View.INVISIBLE
				binding.btnOpenAuthActivity.visibility = View.VISIBLE
			} else {
				binding.clUserInfo.visibility = View.VISIBLE
				binding.btnOpenAuthActivity.visibility = View.INVISIBLE
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}