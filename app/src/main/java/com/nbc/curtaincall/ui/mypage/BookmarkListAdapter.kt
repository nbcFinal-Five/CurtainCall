package com.nbc.curtaincall.ui.mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.MyPageItemBinding
import com.nbc.curtaincall.supabase.model.GetBookmarkModel

class BookmarkListAdapter : ListAdapter<GetBookmarkModel, BookmarkListAdapter.ViewHolder>(MyBookmarkCallback()) {
	inner class ViewHolder(private val binding: MyPageItemBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: GetBookmarkModel) {
			Glide.with(binding.root).load(item.poster).into(binding.ivItem)

			binding.root.setOnClickListener {
				// TODO 상세 넘어가게
				Log.d("debig", item.toString())
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

class MyBookmarkCallback : DiffUtil.ItemCallback<GetBookmarkModel>() {
	override fun areItemsTheSame(oldItem: GetBookmarkModel, newItem: GetBookmarkModel): Boolean {
		return oldItem.id == newItem.id
	}

	override fun areContentsTheSame(oldItem: GetBookmarkModel, newItem: GetBookmarkModel): Boolean {
		return oldItem.id == newItem.id
	}
}
