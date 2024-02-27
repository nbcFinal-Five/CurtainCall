package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nbc.curtaincall.data.model.Db
import com.nbc.curtaincall.databinding.ItemGenreBinding

class GenreAdapter :
    ListAdapter<Db, GenreAdapter.GenreViewHolder>(object : DiffUtil.ItemCallback<Db>() {
        override fun areItemsTheSame(oldItem: Db, newItem: Db): Boolean {
            return oldItem.mt20id == newItem.mt20id
        }
        override fun areContentsTheSame(oldItem: Db, newItem: Db): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Db) {
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