package com.nbc.curtaincall.ui.mypage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.MyPageItemBinding
import com.nbc.curtaincall.supabase.model.GetBookmarkModel
import com.nbc.curtaincall.ui.detail_activity.DetailActivity
import com.nbc.curtaincall.util.Constants

class BookmarkListAdapter(
	private val context: Context,
	private val launcher: ActivityResultLauncher<Intent>
) : ListAdapter<GetBookmarkModel, BookmarkListAdapter.ViewHolder>(MyBookmarkCallback()) {
	inner class ViewHolder(private val binding: MyPageItemBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(item: GetBookmarkModel) {
			Glide.with(binding.root).load(item.poster).into(binding.ivItem)

			binding.root.setOnClickListener {
				val intent = Intent(context, DetailActivity::class.java).apply {
					putExtra(Constants.SHOW_ID, item.mt20id)
					putExtra(Constants.FACILITY_ID, item.mt10id)
				}
				launcher.launch(intent)
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
