package com.nbc.shownect.presentation.detail_activity.er.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nbc.shownect.R
import com.nbc.shownect.databinding.ActivityErBinding
import com.nbc.shownect.ui.detail_activity.er.expectation.ExpectationListAdapter
import com.nbc.shownect.ui.detail_activity.er.review.ReviewListAdapter
import com.nbc.shownect.ui.more.InfiniteScrollListener

class ErActivity : AppCompatActivity() {
	private val erMoreViewModel by lazy { ViewModelProvider(this)[ErMoreViewModel::class.java] }

	private var globalMode: String? = null
	private lateinit var binding: ActivityErBinding

	private val reviewAdapter by lazy { ReviewListAdapter() }
	private val expectationAdapter by lazy { ExpectationListAdapter() }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityErBinding.inflate(layoutInflater)

		setContentView(binding.root)

		val mode = intent.getStringExtra("mode") ?: return finish()
		val mt20id = intent.getStringExtra("mt20id") ?: return finish()

		globalMode = mode

		initTitle(mode)
		initViewModel(mode, mt20id)
		initHandle(mode)
		initScroll(mode, mt20id)
		initData(mode, mt20id)
	}

	private fun initTitle(mode: String) = with(binding) {
		tvLoginTitle.text =
			if (mode == "reviews") "리뷰" else "기대평"
	}

	private fun initData(mode: String, mt20id: String) {
		if (mode == "reviews") {
			erMoreViewModel.loadMoreReviews(mt20id)
		} else {
			erMoreViewModel.loadMoreExpectations(mt20id)
		}
	}

	private fun initScroll(mode: String, mt20id: String) = with(binding) {
		val rv = if (mode == "reviews") rvReviews else rvExpectations
		val adapter = if (mode == "reviews") reviewAdapter else expectationAdapter

		val layoutManager = LinearLayoutManager(this@ErActivity)

		rv.layoutManager = layoutManager
		rv.adapter = adapter

		rv.addOnScrollListener(InfiniteScrollListener(layoutManager = layoutManager) {
			erMoreViewModel.loadMoreReviews(mt20id)
		})
	}

	private fun initViewModel(mode: String, mt20id: String) {
		if (mode == "reviews") {
			erMoreViewModel.reviewList.observe(this) {
				reviewAdapter.submitList(it)

				if (it.isEmpty()) {
					binding.tvMoreEmpty.visibility = View.VISIBLE
					binding.tvMoreEmpty.text = getText(R.string.showcase_empty)
				} else {
					binding.tvMoreEmpty.visibility = View.INVISIBLE
				}
			}
		} else {
			erMoreViewModel.expectationsList.observe(this) {
				expectationAdapter.submitList(it)

				if (it.isEmpty()) {
					binding.tvMoreEmpty.visibility = View.VISIBLE
					binding.tvMoreEmpty.text = "기대평을 남겨주시면\\n당신의 기대평 목록이 담깁니다"
				} else {
					binding.tvMoreEmpty.visibility = View.INVISIBLE
				}
			}
		}

		erMoreViewModel.isEnd.observe(this) {
			binding.clSkeleton.visibility = if (it) View.GONE else View.VISIBLE
		}
	}

	private fun initHandle(mode: String) = with(binding) {
		btnAuthBack.setOnClickListener {
			finish()
		}

		when (mode) {
			"reviews" -> rvReviews.visibility = android.view.View.VISIBLE
			"expectations" -> rvExpectations.visibility = android.view.View.VISIBLE
		}
	}
}