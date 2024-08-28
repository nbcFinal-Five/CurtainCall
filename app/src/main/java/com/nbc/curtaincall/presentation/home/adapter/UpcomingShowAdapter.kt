package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbc.curtaincall.databinding.ItemUpcomingShowBinding
import com.nbc.curtaincall.presentation.home.adapter.DiffCallback
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.util.ViewUtil.setPoster


class UpcomingShowAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<ShowItem, UpcomingShowAdapter.UpcomingShowViewHolder>(DiffCallback()) {

    inner class UpcomingShowViewHolder(private val binding: ItemUpcomingShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem) {
            item as ShowItem.UpcomingShowItem
            with(binding) {
                ivUpcomingShowPoster.setPoster(itemView, item.posterPath)
                tvPlaceName.text = item.placeName
                tvUpcomingShowPeriod.text = "~ ${item.periodTo}"
                tvPerformanceName.text = item.title
                tvShowingState.text = item.showingState
                tvGenre.text = item.genre
                ivUpcomingShowPoster.setOnClickListener {
                    item.showId?.let { id -> onClick(id) }
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