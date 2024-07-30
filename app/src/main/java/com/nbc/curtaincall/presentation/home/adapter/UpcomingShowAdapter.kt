package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.data.model.DbResponse
import com.nbc.curtaincall.databinding.ItemUpcomingShowBinding


class UpcomingShowAdapter(private val listener: PosterClickListener? = null) :
    ListAdapter<DbResponse, UpcomingShowAdapter.UpcomingShowViewHolder>(object :
        DiffUtil.ItemCallback<DbResponse>() {
        override fun areItemsTheSame(oldItem: DbResponse, newItem: DbResponse): Boolean {
            return oldItem.showId == newItem.showId
        }

        override fun areContentsTheSame(oldItem: DbResponse, newItem: DbResponse): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class UpcomingShowViewHolder(private val binding: ItemUpcomingShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DbResponse) {
            with(binding) {
                Glide.with(itemView).load(item.posterPath).into(ivUpcomingShowPoster)
                tvPlaceName.text = item.performanceName
                tvUpcomingShowPeriod.text = "~ ${item.prfpdto}"
                tvPerformanceName.text = item.fcltynm
                tvShowingState.text = item.prfstate
                tvGenre.text = item.genreName
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