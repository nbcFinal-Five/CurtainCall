package com.nbc.curtaincall.ui.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nbc.curtaincall.databinding.ActivityMoreBinding
import com.nbc.curtaincall.ui.UserViewModel
import com.nbc.curtaincall.ui.mypage.BookmarkListAdapter
import com.nbc.curtaincall.ui.mypage.GridItemDecoration
import com.nbc.curtaincall.ui.mypage.ReviewListAdapter

class MoreActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMoreBinding

	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }
	private val moreViewModel by lazy { ViewModelProvider(this)[MoreViewModel::class.java] }

	private val reviewAdapter by lazy { ReviewListAdapter() }
	private val bookmarkAdapter by lazy { BookmarkListAdapter() }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMoreBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val mode = intent.getStringExtra("mode") ?: return finish()

		initViewModel()
		initHandle(mode)
		initScroll(mode)
		moreViewModel.loadMoreReviews(userViewModel.userInfo.value?.id ?: "")
	}

	private fun initScroll(mode: String) = with(binding) {
		val rv = if (mode == "reviews") rvShowcase else rvBookmarks
		val adapter = if (mode == "reviews") reviewAdapter else bookmarkAdapter

		val density = resources.displayMetrics.density
		rv.layoutManager = GridLayoutManager(this@MoreActivity, 3)
		rv.adapter = adapter
		rv.addItemDecoration(
			GridItemDecoration(
				horizontalSpacing = (24 * density).toInt(),
				verticalSpacing = (12 * density).toInt()
			)
		)
	}

	private fun initViewModel() {
		moreViewModel.reviewList.observe(this) {
			reviewAdapter.submitList(it)
		}

		moreViewModel.reviewIsEnd.observe(this) {
			// TODO Skeleton
		}
	}

	private fun initHandle(mode: String) = with(binding) {
		btnAuthBack.setOnClickListener {
			finish()
		}

		when (mode) {
			"reviews" -> {
				rvShowcase.visibility = View.VISIBLE

			}

			"likes" -> {
				rvBookmarks.visibility = View.VISIBLE
			}
		}
	}
}