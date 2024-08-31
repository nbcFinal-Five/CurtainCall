package com.nbc.curtaincall.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbc.curtaincall.databinding.ItemBookmarkBinding
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.util.ViewUtil.setPoster

class BookmarkAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<ShowItem.DetailShowItem, BookmarkAdapter.InterestViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        return InterestViewHolder(
            ItemBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ShowItem.DetailShowItem>() {
            override fun areItemsTheSame(
                oldItem: ShowItem.DetailShowItem,
                newItem: ShowItem.DetailShowItem
            ): Boolean = oldItem.showId == newItem.showId

            override fun areContentsTheSame(
                oldItem: ShowItem.DetailShowItem,
                newItem: ShowItem.DetailShowItem
            ): Boolean = oldItem == newItem

        }
    }

    inner class InterestViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem.DetailShowItem) {
            with(binding) {
                ivHomeGenrePoster.setPoster(itemView, item.posterPath)
                tvGenre.text = item.genre
                tvShowingState.text = item.showState
                tvGenrePeriod.text = "~ ${item.periodTo}"
                tvPlaceName.text = item.placeName
                tvPerformanceName.text = item.title
                ivHomeGenrePoster.setOnClickListener {
                    item.showId?.let { id -> onClick(id) }
                }
            }
        }
    }

}