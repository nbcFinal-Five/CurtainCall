package com.nbc.curtaincall.ui.more

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteScrollListener(
	private val layoutManager: LinearLayoutManager,
	private val onLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

	private val visibleThreshold = 1

	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)

		val totalItemCount = layoutManager.itemCount
		val lastVisibleItem = layoutManager.findLastVisibleItemPosition()


		if (totalItemCount - lastVisibleItem <= visibleThreshold) {
			onLoadMore()
		}
	}

}