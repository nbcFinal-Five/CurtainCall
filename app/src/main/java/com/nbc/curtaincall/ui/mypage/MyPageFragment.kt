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

class MyPageFragment : Fragment(), LogoutDialogFragment.LogoutDialogListener, QuitDialogFragment.QuitDialogListener {
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

	override fun onQuitConfirmed() {
		userViewModel.setUser()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentMyPageBinding.inflate(inflater, container, false)
		val root: View = binding.root

		userViewModel.setUser()

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

		btnDattai.setOnClickListener {
			val dialog = QuitDialogFragment()
			dialog.setListener(this@MyPageFragment)
			dialog.show(childFragmentManager, "Logout Dialog")
		}
	}

	private fun initViewModel() = with(binding) {
		userViewModel.userInfo.observe(viewLifecycleOwner) {
			if (it == null) {
				clUserInfo.visibility = View.INVISIBLE
				cvOpenAuthActivity.visibility = View.VISIBLE
				tvNickname.text = ""
				tvEmail.text = ""
				tvSignOut.visibility = View.INVISIBLE

				llReviewsMore.visibility = View.INVISIBLE
				llLikesMore.visibility = View.INVISIBLE

				llAuth.visibility = View.VISIBLE
				myPageViewModel.clear()
			} else {
				clUserInfo.visibility = View.VISIBLE
				cvOpenAuthActivity.visibility = View.INVISIBLE
				tvNickname.text = it.userMetadata!!["name"].toString().removeSurrounding("\"")
				tvEmail.text = it.email
				tvSignOut.visibility = View.VISIBLE

				llReviewsMore.visibility = View.VISIBLE
				llLikesMore.visibility = View.VISIBLE

				llAuth.visibility = View.INVISIBLE

				myPageViewModel.setReview(it.id)
				myPageViewModel.setBookmarks(it.id)
				myPageViewModel.setReviewCount(it.id)
			}
		}

		// reviews
		myPageViewModel.reviews.observe(viewLifecycleOwner) {
			if (it == null) {
				rvShowcase.visibility = View.INVISIBLE
				tvShowcaseDetail.visibility = View.VISIBLE
			} else {
				if (it.isEmpty()) {
					rvShowcase.visibility = View.INVISIBLE
					tvShowcaseDetail.visibility = View.VISIBLE
				} else {
					rvShowcase.visibility = View.VISIBLE
					tvShowcaseDetail.visibility = View.INVISIBLE

					reviewAdapter.submitList(it.take(6))
				}
			}
		}

		myPageViewModel.isReviewLoading.observe(viewLifecycleOwner) {
			if (it) {
				clReviewSkeleton.visibility = View.VISIBLE
				rvShowcase.visibility = View.INVISIBLE
				tvShowcaseDetail.visibility = View.INVISIBLE
			} else {
				clReviewSkeleton.visibility = View.INVISIBLE
			}
		}

		// reviews count
		myPageViewModel.reviewCount.observe(viewLifecycleOwner) {
			if (it != null) tvReviewCount.text = it.toString() + getString(R.string.count)
		}

		myPageViewModel.isReviewCountLoading.observe(viewLifecycleOwner) {
			if (it) {
				clCountSkeleton.visibility = View.VISIBLE
				tvReviewCount.visibility = View.INVISIBLE
			} else {
				tvReviewCount.visibility = View.VISIBLE
				clCountSkeleton.visibility = View.INVISIBLE
			}
		}

		// bookmarks
		myPageViewModel.bookmarks.observe(viewLifecycleOwner) {
			if (it == null) {
				rvBookmarks.visibility = View.INVISIBLE
				tvLikeDetail.visibility = View.VISIBLE
			} else {
				if (it.isEmpty()) {
					rvBookmarks.visibility = View.INVISIBLE
					tvLikeDetail.visibility = View.VISIBLE
				} else {
					rvBookmarks.visibility = View.VISIBLE
					tvLikeDetail.visibility = View.INVISIBLE

					bookmarkAdapter.submitList(it.take(6))
				}
			}
		}

		myPageViewModel.isBookmarkLoading.observe(viewLifecycleOwner) {
			if (it) {
				clBookmarkSkeleton.visibility = View.VISIBLE
				rvBookmarks.visibility = View.INVISIBLE
				tvLikeDetail.visibility = View.INVISIBLE
			} else {
				clBookmarkSkeleton.visibility = View.INVISIBLE

				tvReviewCount.visibility = View.VISIBLE
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}