package com.nbc.curtaincall.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nbc.curtaincall.R
import com.nbc.curtaincall.ui.auth.AuthActivity
import com.nbc.curtaincall.databinding.FragmentMyPageBinding
import com.nbc.curtaincall.ui.UserViewModel

class MyPageFragment : Fragment() {
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }
	private val myPageViewModel by lazy { ViewModelProvider(this)[MyPageViewModel::class.java] }

	private var _binding: FragmentMyPageBinding? = null
	private val binding get() = _binding!!

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		userViewModel.setUser()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
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
			val intent = Intent(requireActivity(), AuthActivity::class.java)
			launcher.launch(intent)
		}

		tvSignOut.setOnClickListener {
			userViewModel.signOut()
		}
	}

	private fun initViewModel() {
		userViewModel.userInfo.observe(viewLifecycleOwner) {
			if (it == null) {
				binding.clUserInfo.visibility = View.INVISIBLE
				binding.cvOpenAuthActivity.visibility = View.VISIBLE
				binding.tvNickname.text = ""
				binding.tvEmail.text = ""
				binding.tvSignOut.visibility = View.INVISIBLE

				myPageViewModel.clearReviews()
			} else {
				binding.clUserInfo.visibility = View.VISIBLE
				binding.cvOpenAuthActivity.visibility = View.INVISIBLE
				binding.tvNickname.text = it.userMetadata!!["name"].toString().removeSurrounding("\"")
				binding.tvEmail.text = it.email
				binding.tvSignOut.visibility = View.VISIBLE

				myPageViewModel.setReview(it.id)
			}
		}

		// reviews
		myPageViewModel.reviews.observe(viewLifecycleOwner) {
			if (it != null) {
				binding.tvReviewCount.text = it.size.toString() + getString(R.string.count)
			}
		}

		myPageViewModel.isReviewLoading.observe(viewLifecycleOwner) {
			if (it) binding.tvReviewCount.text = "불러오는 중"
		}

		// bookmarks
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}