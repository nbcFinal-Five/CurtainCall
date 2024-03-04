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
import androidx.recyclerview.widget.GridLayoutManager
import com.nbc.curtaincall.R
import com.nbc.curtaincall.ui.auth.AuthActivity
import com.nbc.curtaincall.databinding.FragmentMyPageBinding
import com.nbc.curtaincall.ui.UserViewModel
import com.nbc.curtaincall.ui.more.MoreActivity

class MyPageFragment : Fragment(), LogoutDialogFragment.LogoutDialogListener {
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }
	private val myPageViewModel by lazy { ViewModelProvider(this)[MyPageViewModel::class.java] }

	private var _binding: FragmentMyPageBinding? = null
	private val binding get() = _binding!!

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		userViewModel.setUser()
	}

	private val reviewAdapter by lazy { ReviewListAdapter() }
	private val bookmarkAdapter by lazy { BookmarkListAdapter() }

	override fun onLogoutConfirmed() {
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
		initList()
	}

	private fun initList() {
		val rvReview = binding.rvShowcase
		val rvBookmark = binding.rvBookmarks

		val density = resources.displayMetrics.density
		rvReview.layoutManager = GridLayoutManager(requireContext(), 3)
		rvReview.adapter = reviewAdapter
		rvReview.addItemDecoration(
			GridItemDecoration(
				horizontalSpacing = (24 * density).toInt(),
				verticalSpacing = (12 * density).toInt()
			)
		)

		rvBookmark.layoutManager = GridLayoutManager(requireContext(), 3)
		rvBookmark.adapter = bookmarkAdapter
		rvBookmark.addItemDecoration(
			GridItemDecoration(
				horizontalSpacing = (24 * density).toInt(),
				verticalSpacing = (12 * density).toInt()
			)
		)
	}

	private fun initHandle() = with(binding) {
		btnOpenAuthActivity.setOnClickListener {
			val intent = Intent(requireActivity(), AuthActivity::class.java)
			launcher.launch(intent)
		}

		tvSignOut.setOnClickListener {
			val dialog = LogoutDialogFragment()
			dialog.setListener(this@MyPageFragment)
			dialog.show(childFragmentManager, "Logout Dialog")
		}

		llReviewsMore.setOnClickListener {
			val intent = Intent(requireActivity(), MoreActivity::class.java)
			intent.putExtra("mode", "reviews")

			startActivity(intent)
		}

		llLikesMore.setOnClickListener {
			val intent = Intent(requireActivity(), MoreActivity::class.java)
			intent.putExtra("mode", "likes")

			startActivity(intent)
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

				binding.llReviewsMore.visibility = View.INVISIBLE
				binding.llLikesMore.visibility = View.INVISIBLE

				myPageViewModel.clear()
			} else {
				binding.clUserInfo.visibility = View.VISIBLE
				binding.cvOpenAuthActivity.visibility = View.INVISIBLE
				binding.tvNickname.text = it.userMetadata!!["name"].toString().removeSurrounding("\"")
				binding.tvEmail.text = it.email
				binding.tvSignOut.visibility = View.VISIBLE

				binding.llReviewsMore.visibility = View.VISIBLE
				binding.llLikesMore.visibility = View.VISIBLE

				myPageViewModel.setReview(it.id)
				myPageViewModel.setBookmarks(it.id)
			}
		}

		// reviews
		myPageViewModel.reviews.observe(viewLifecycleOwner) {
			if (it == null) {
				binding.rvShowcase.visibility = View.INVISIBLE
				binding.tvShowcaseDetail.visibility = View.VISIBLE
			} else {
				binding.tvReviewCount.text = it.size.toString() + getString(R.string.count)

				if (it.isEmpty()) {
					binding.rvShowcase.visibility = View.INVISIBLE
					binding.tvShowcaseDetail.visibility = View.VISIBLE
				} else {
					binding.rvShowcase.visibility = View.VISIBLE
					binding.tvShowcaseDetail.visibility = View.INVISIBLE

					reviewAdapter.submitList(it.take(6))
				}
			}
		}

		myPageViewModel.isReviewLoading.observe(viewLifecycleOwner) {
			if (it) {
				binding.clCountSkeleton.visibility = View.VISIBLE
				binding.clReviewSkeleton.visibility = View.VISIBLE

				binding.tvReviewCount.visibility = View.INVISIBLE
				binding.rvShowcase.visibility = View.INVISIBLE
				binding.tvShowcaseDetail.visibility = View.INVISIBLE
			} else {
				binding.clReviewSkeleton.visibility = View.INVISIBLE
				binding.clCountSkeleton.visibility = View.INVISIBLE

				binding.tvReviewCount.visibility = View.VISIBLE
			}
		}

		// bookmarks
		myPageViewModel.bookmarks.observe(viewLifecycleOwner) {
			if (it == null) {
				binding.rvBookmarks.visibility = View.INVISIBLE
				binding.tvLikeDetail.visibility = View.VISIBLE
			} else {
				if (it.isEmpty()) {
					binding.rvBookmarks.visibility = View.INVISIBLE
					binding.tvLikeDetail.visibility = View.VISIBLE
				} else {
					binding.rvBookmarks.visibility = View.VISIBLE
					binding.tvLikeDetail.visibility = View.INVISIBLE

					bookmarkAdapter.submitList(it.take(6))
				}
			}
		}

		myPageViewModel.isBookmarkLoading.observe(viewLifecycleOwner) {
			if (it) {
				binding.clBookmarkSkeleton.visibility = View.VISIBLE
				binding.rvBookmarks.visibility = View.INVISIBLE
				binding.tvLikeDetail.visibility = View.INVISIBLE
			} else {
				binding.clBookmarkSkeleton.visibility = View.INVISIBLE

				binding.tvReviewCount.visibility = View.VISIBLE
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}