package com.nbc.shownect.ui.detail_activity.er.review

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nbc.shownect.R
import com.nbc.shownect.databinding.FragmentReviewBinding
import com.nbc.shownect.supabase.model.PostReviewModel
import com.nbc.shownect.ui.UserViewModel
import com.nbc.shownect.ui.auth.AuthActivity

class ReviewFragment(
	private val mt20id: String,
	private val poster: String
) : Fragment() {
	private val reviewViewModel by lazy { ViewModelProvider(this)[ReviewViewModel::class.java] }
	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }

	private lateinit var binding: FragmentReviewBinding

	private var isError = false

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		userViewModel.setUser()
	}

	private lateinit var listAdapter: ReviewListAdapter

	private val pointGroup by lazy {
		listOf(
			binding.ivPoint1,
			binding.ivPoint2,
			binding.ivPoint3,
			binding.ivPoint4,
			binding.ivPoint5
		)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentReviewBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		initHandle()
		initViewModel()
		initList()
		initData()
	}

	private fun initList() {
		listAdapter = ReviewListAdapter()

		binding.rvReviews.adapter = listAdapter
		binding.rvReviews.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
	}

	private fun initData() {
		reviewViewModel.setTotalCount(mt20id)
		reviewViewModel.setList(mt20id)
	}

	private fun initHandle() = with(binding) {
		pointGroup.forEachIndexed { index, view ->
			view.setOnClickListener {
				reviewViewModel.setPoint(index + 1)
			}
		}

		etReview.addTextChangedListener {
			reviewViewModel.setComment(it.toString())
		}

		btnSubmit.setOnClickListener {
			hideKeyboard()
			isError = true

			val point = reviewViewModel.point.value
			val comment = reviewViewModel.comment.value

			reviewViewModel.setComment(comment)
			reviewViewModel.setPoint(point)

			if (isValidComment(comment) && point != null) {
				val userId = userViewModel.userInfo.value?.id

				if (userId == null) {
					val intent = Intent(requireActivity(), AuthActivity::class.java)
					launcher.launch(intent)
					return@setOnClickListener
				}

				val model = PostReviewModel(
					userId = userId,
					mt20id = mt20id,
					poster = poster,
					point = point,
					comment = comment!!
				)

				reviewViewModel.createReview(
					model = model,
					context = requireContext(),
					errorMessage = getString(R.string.already_point)
				)
			}
		}
	}

	private fun initViewModel() {
		reviewViewModel.point.observe(viewLifecycleOwner) {
			with(binding) {
				if (isError) {
					if (it == null) {
						tvReviewWarning.visibility = View.VISIBLE
						return@observe
					} else {
						tvReviewWarning.visibility = View.INVISIBLE
					}
				}

				if (it == null) return@observe

				tvPoints.text = getString(R.string.start_point_title) + " ${it}점"
				pointGroup.forEachIndexed { index, pointView ->
					pointView.setImageResource(if (index <= it - 1) R.drawable.full_star else R.drawable.empty_star)
				}
			}
		}

		reviewViewModel.comment.observe(viewLifecycleOwner) {
			if (isError) {
				binding.tvCommentWarning.visibility = if (isValidComment(it!!)) View.INVISIBLE else View.VISIBLE
				binding.btnSubmit.isClickable = isValidComment(it)
			}
		}

		reviewViewModel.isCreateReviewLoading.observe(viewLifecycleOwner) {
			if (it == null) return@observe

			binding.btnSubmit.isClickable = !it
		}

		reviewViewModel.totalCount.observe(viewLifecycleOwner) {
			binding.btnMore.text = getString(R.string.more_review_text) + "($it)"
		}

		reviewViewModel.comments.observe(viewLifecycleOwner) {
			if (it.isEmpty()) {
				binding.tvEmptyReviews.visibility = View.VISIBLE
				binding.rvReviews.visibility = View.INVISIBLE
				binding.btnMore.visibility = View.INVISIBLE
			} else {
				binding.tvEmptyReviews.visibility = View.INVISIBLE
				binding.rvReviews.visibility = View.VISIBLE
				binding.btnMore.visibility = View.VISIBLE

				listAdapter.submitList(it)
			}
		}
	}

	private fun isValidComment(comment: String?): Boolean {
		if (comment == null) return false
		return (comment.length >= 10) && (comment.length <= 30)
	}

	private fun hideKeyboard() {
		val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.hideSoftInputFromWindow(view?.windowToken, 0)
	}
}