package com.nbc.shownect.ui.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nbc.shownect.R
import com.nbc.shownect.databinding.ActivityMoreBinding
import com.nbc.shownect.ui.UserViewModel
import com.nbc.shownect.ui.mypage.BookmarkListAdapter
import com.nbc.shownect.ui.mypage.GridItemDecoration
import com.nbc.shownect.ui.mypage.ReviewListAdapter

class MoreActivity : AppCompatActivity() {
	private var globalMode: String? = null
	private lateinit var binding: ActivityMoreBinding

	private val userViewModel by lazy { ViewModelProvider(this)[UserViewModel::class.java] }
	private val moreViewModel by lazy { ViewModelProvider(this)[MoreViewModel::class.java] }

	private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		userViewModel.setUser()

		if (globalMode != null) {
			Log.d("debug", globalMode.toString())
			initData(globalMode!!)
		}
	}

	private val reviewAdapter by lazy { ReviewListAdapter(this, launcher) }
	private val bookmarkAdapter by lazy { BookmarkListAdapter(this, launcher) }

	private val layoutManager by lazy { GridLayoutManager(this@MoreActivity, 3) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMoreBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val mode = intent.getStringExtra("mode") ?: return finish()

		globalMode = mode

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
				horizontalSpacing = (12 * density).toInt(),
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

				if (it.isEmpty()) {
					binding.tvMoreEmpty.visibility = View.VISIBLE
					binding.tvMoreEmpty.text = getText(R.string.showcase_empty)
				} else {
					binding.tvMoreEmpty.visibility = View.INVISIBLE
				}
			}


		} else {
			moreViewModel.bookmarksList.observe(this) {
				bookmarkAdapter.submitList(it)

				if (it.isEmpty()) {
					binding.tvMoreEmpty.visibility = View.VISIBLE
					binding.tvMoreEmpty.text = getText(R.string.bookmarks_empty)
				} else {
					binding.tvMoreEmpty.visibility = View.INVISIBLE
				}
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