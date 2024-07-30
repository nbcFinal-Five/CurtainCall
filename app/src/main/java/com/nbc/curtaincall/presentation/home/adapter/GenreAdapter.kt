package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.ItemGenreBinding
import com.nbc.curtaincall.data.model.DbResponse

class GenreAdapter(private val listener: PosterClickListener? = null) :
    ListAdapter<DbResponse, GenreAdapter.GenreViewHolder>(object :
        DiffUtil.ItemCallback<DbResponse>() {
        override fun areItemsTheSame(oldItem: DbResponse, newItem: DbResponse): Boolean {
            return oldItem.showId == newItem.showId
        }

        override fun areContentsTheSame(oldItem: DbResponse, newItem: DbResponse): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DbResponse) {
            with(binding) {
                Glide.with(itemView).load(item.posterPath).into(ivHomeGenrePoster)
                tvGenre.text = item.genreName
                tvShowingState.text = item.prfstate
                tvGenre.text = item.genreName
                tvGenrePeriod.text = "~ ${item.prfpdto}"
                tvPlaceName.text = item.fcltynm
                tvPerformanceName.text = item.performanceName
                ivHomeGenrePoster.setOnClickListener {
                    item.showId?.let { id -> listener?.posterClicked(id) }
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