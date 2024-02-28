package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nbc.curtaincall.fetch.model.DbResponse
import com.nbc.curtaincall.databinding.ItemGenreBinding

class GenreAdapter :
    ListAdapter<DbResponse, GenreAdapter.GenreViewHolder>(object : DiffUtil.ItemCallback<DbResponse>() {
        override fun areItemsTheSame(oldItem: DbResponse, newItem: DbResponse): Boolean {
            return oldItem.mt20id == newItem.mt20id
        }

        override fun areContentsTheSame(oldItem: DbResponse, newItem: DbResponse): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DbResponse) {
            with(binding) {
                tvGenre.text = item.genrenm
                tvShowingState.text = item.prfstate
                tvGenre.text = item.genrenm
                tvGenrePeriod.text = "~ ${item.prfpdto}"
                tvPlaceName.text = item.fcltynm
                tvPerformanceName.text = item.prfnm
                ivHomeGenrePoster.load(item.poster)
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