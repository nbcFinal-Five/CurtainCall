package com.nbc.shownect.ui.detail_activity.expectation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import com.nbc.shownect.databinding.ExpectationItemBinding
import com.nbc.shownect.supabase.model.GetExpectationModel

class ExpectationListAdapter :
	ListAdapter<GetExpectationModel, ExpectationListAdapter.ViewHolder>(ExpectationDiffCallback()) {
	inner class ViewHolder(private val binding: ExpectationItemBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: GetExpectationModel) = with(binding) {
			tvName.text = item.profile.name
			tvComment.text = item.comment

			val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
			val dateTime = LocalDateTime.parse(item.createdAt, inputFormatter)

			// 출력 형식 지정
			val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
			val outputDateString = dateTime.format(outputFormatter)

			tvCreatedAt.text = outputDateString
		}
	}

	override fun onCreateViewHolder(group: ViewGroup, position: Int): ViewHolder {
		return ViewHolder(ExpectationItemBinding.inflate(LayoutInflater.from(group.context), group, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}
}

class ExpectationDiffCallback : DiffUtil.ItemCallback<GetExpectationModel>() {
	override fun areItemsTheSame(oldItem: GetExpectationModel, newItem: GetExpectationModel): Boolean {
		return oldItem.id == newItem.id
	}

	override fun areContentsTheSame(oldItem: GetExpectationModel, newItem: GetExpectationModel): Boolean {
		return oldItem.id == newItem.id
	}
}
