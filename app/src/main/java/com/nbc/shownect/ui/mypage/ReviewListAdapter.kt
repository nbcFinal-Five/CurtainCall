package com.nbc.shownect.ui.mypage

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbc.shownect.databinding.MyPageItemBinding
import com.nbc.shownect.supabase.model.GetReviewModel

class ReviewListAdapter : ListAdapter<GetReviewModel, ReviewListAdapter.ViewHolder>(MyDiffCallback()) {
	inner class ViewHolder(private val binding: MyPageItemBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: GetReviewModel) {
			binding.ivItem.load(item.poster)

			binding.root.setOnClickListener {
				// TODO 상세 넘어가게
			}
		}
	}

	override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
		val viewHolder = ViewHolder(MyPageItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

		return viewHolder
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}

class MyDiffCallback : DiffUtil.ItemCallback<GetReviewModel>() {
	override fun areItemsTheSame(oldItem: GetReviewModel, newItem: GetReviewModel): Boolean {
		return oldItem.id == newItem.id
	}

	override fun areContentsTheSame(oldItem: GetReviewModel, newItem: GetReviewModel): Boolean {
		return oldItem.id == newItem.id
	}
}

class GridItemDecoration(
	private val horizontalSpacing: Int,
	private val verticalSpacing: Int
) :
	RecyclerView.ItemDecoration() {

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		val position = parent.getChildAdapterPosition(view)
		val spanCount = (parent.layoutManager as GridLayoutManager).spanCount

		val column = position % spanCount

		outRect.left = column * horizontalSpacing / spanCount
		outRect.right = horizontalSpacing - (column + 1) * horizontalSpacing / spanCount

		if (position >= spanCount) {
			outRect.top = verticalSpacing
		}
	}
}