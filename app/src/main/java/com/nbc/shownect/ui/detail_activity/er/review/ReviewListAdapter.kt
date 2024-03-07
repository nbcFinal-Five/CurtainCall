package com.nbc.shownect.ui.detail_activity.er.review

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbc.shownect.R
import com.nbc.shownect.databinding.ReviewItemBinding
import com.nbc.shownect.supabase.model.GetReviewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReviewListAdapter :
	ListAdapter<GetReviewModel, ReviewListAdapter.ViewHolder>(ReviewDiffCallback()) {
	inner class ViewHolder(private val binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: GetReviewModel) = with(binding) {
			tvName.text = item.profile.name
			tvComment.text = item.comment

			val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
			val dateTime = LocalDateTime.parse(item.createdAt, inputFormatter)

			// 출력 형식 지정
			val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
			val outputDateString = dateTime.format(outputFormatter)

			tvCreatedAt.text = outputDateString

			listOf(
				ivPoint1,
				ivPoint2,
				ivPoint3,
				ivPoint4,
				ivPoint5
			).forEachIndexed { index, pointView ->
				pointView.setImageResource(if (index <= item.point - 1) R.drawable.full_star else R.drawable.empty_star)
			}
		}
	}

	override fun onCreateViewHolder(group: ViewGroup, position: Int): ViewHolder {
		return ViewHolder(ReviewItemBinding.inflate(LayoutInflater.from(group.context), group, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}

class ReviewDiffCallback : DiffUtil.ItemCallback<GetReviewModel>() {
	override fun areItemsTheSame(oldItem: GetReviewModel, newItem: GetReviewModel): Boolean {
		return oldItem.id == newItem.id
	}

	override fun areContentsTheSame(oldItem: GetReviewModel, newItem: GetReviewModel): Boolean {
		return oldItem.id == newItem.id
	}
}
