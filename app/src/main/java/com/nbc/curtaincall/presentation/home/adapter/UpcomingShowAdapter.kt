package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.ItemUpcomingShowBinding
import com.nbc.curtaincall.presentation.home.adapter.DiffCallback
import com.nbc.curtaincall.presentation.model.ShowItem


class UpcomingShowAdapter(private val listener: PosterClickListener? = null) :
    ListAdapter<ShowItem, UpcomingShowAdapter.UpcomingShowViewHolder>(DiffCallback()) {
    inner class UpcomingShowViewHolder(private val binding: ItemUpcomingShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem) {
            item as ShowItem.UpcomingShowItem
            with(binding) {
                Glide.with(itemView).load(item.posterPath).into(ivUpcomingShowPoster)
                tvPlaceName.text = item.placeName
                tvUpcomingShowPeriod.text = "~ ${item.periodTo}"
                tvPerformanceName.text = item.title
                tvShowingState.text = item.showingState
                tvGenre.text = item.genre
                ivUpcomingShowPoster.setOnClickListener {
                    item.showId?.let { id -> listener?.posterClicked(id) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingShowViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemUpcomingShowBinding.inflate(inflater, parent, false)
        return UpcomingShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingShowViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}