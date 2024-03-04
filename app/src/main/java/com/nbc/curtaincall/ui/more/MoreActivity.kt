package com.nbc.curtaincall.ui.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nbc.curtaincall.R
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

	private val layoutManager by lazy { GridLayoutManager(this@MoreActivity, 3) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMoreBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val mode = intent.getStringExtra("mode") ?: return finish()

		initTitle(mode)
		initViewModel(mode)
		initHandle(mode)
		initScroll(mode)
		initData(mode)
	}

	private fun initTitle(mode: String) = with(binding) {
		tvLoginTitle.text = getString(if (mode == "reviews") R.string.my_page_showcase else R.string.bookmark_title)
	}

	private fun initData(mode: String) {
		if (mode == "reviews") {
			moreViewModel.loadMoreReviews(userViewModel.userInfo.value?.id ?: "")
		} else {
			moreViewModel.loadMoreBookmarks(userViewModel.userInfo.value?.id ?: "")
		}
	}

	private fun initScroll(mode: String) = with(binding) {
		val rv = if (mode == "reviews") rvShowcase else rvBookmarks
		val adapter = if (mode == "reviews") reviewAdapter else bookmarkAdapter

		val density = resources.displayMetrics.density
		rv.layoutManager = layoutManager
		rv.adapter = adapter
		rv.addItemDecoration(
			GridItemDecoration(
				horizontalSpacing = (24 * density).toInt(),
				verticalSpacing = (12 * density).toInt()
			)
		)

		rv.addOnScrollListener(InfiniteScrollListener(layoutManager = layoutManager) {
			moreViewModel.loadMoreReviews(userViewModel.userInfo.value!!.id)
		})
	}

	private fun initViewModel(mode: String) {
		if (mode == "reviews") {
			moreViewModel.reviewList.observe(this) {
				reviewAdapter.submitList(it)
			}


		} else {
			moreViewModel.bookmarksList.observe(this) {
				bookmarkAdapter.submitList(it)
			}
		}

		moreViewModel.isEnd.observe(this) {
			binding.clSkeleton.visibility = if (it) View.GONE else View.VISIBLE
		}
	}

	private fun initHandle(mode: String) = with(binding) {
		btnAuthBack.setOnClickListener {
			finish()
		}

		when (mode) {
			"reviews" -> rvShowcase.visibility = View.VISIBLE
			"likes" -> rvBookmarks.visibility = View.VISIBLE
		}
	}
}