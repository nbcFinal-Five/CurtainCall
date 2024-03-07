package com.nbc.curtaincall.ui.detail.expectation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbc.curtaincall.databinding.ExpectationItemBinding
import com.nbc.curtaincall.supabase.model.GetExpectationModel

class ExpectationListAdapter :
	ListAdapter<GetExpectationModel, ExpectationListAdapter.ViewHolder>(ExpectationDiffCallback()) {
	inner class ViewHolder(private val binding: ExpectationItemBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: GetExpectationModel) = with(binding) {
			tvName.text = item.profile.name
			tvComment.text = item.comment
			tvCreatedAt.text = item.createdAt
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
