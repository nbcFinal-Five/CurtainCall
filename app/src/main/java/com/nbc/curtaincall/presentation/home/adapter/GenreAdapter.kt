package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbc.curtaincall.databinding.ItemGenreBinding
import com.nbc.curtaincall.presentation.home.adapter.DiffCallback
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.util.ViewUtil.setPoster

class GenreAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<ShowItem, GenreAdapter.GenreViewHolder>(DiffCallback()) {
    inner class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem) {
            item as ShowItem.GenreItem
            with(binding) {
                ivHomeGenrePoster.setPoster(itemView, item.posterPath)
                tvGenre.text = item.genre
                tvShowingState.text = item.showingState
                tvGenrePeriod.text = "~ ${item.periodTo}"
                tvPlaceName.text = item.placeName
                tvPerformanceName.text = item.title
                ivHomeGenrePoster.setOnClickListener {
                    item.showId?.let { id -> onClick(id) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemGenreBinding.inflate(inflater, parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}