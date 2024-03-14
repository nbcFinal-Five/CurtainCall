package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.ItemUpcomingShowBinding
import com.nbc.curtaincall.fetch.model.DbResponse


class UpcomingShowAdapter(private val listener: PosterClickListener? = null) :
    ListAdapter<DbResponse, UpcomingShowAdapter.UpcomingShowViewHolder>(object :
        DiffUtil.ItemCallback<DbResponse>() {
        override fun areItemsTheSame(oldItem: DbResponse, newItem: DbResponse): Boolean {
            return oldItem.mt20id == newItem.mt20id
        }

        override fun areContentsTheSame(oldItem: DbResponse, newItem: DbResponse): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class UpcomingShowViewHolder(private val binding: ItemUpcomingShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DbResponse) {
            with(binding) {
                Glide.with(itemView).load(item.poster).into(ivHomeUpcomingShowPoster)
                tvPerformanceName.text = item.prfnm
                tvPeriod.text = "${item.prfpdfrom} ~ ${item.prfpdto}"
                tvFacilityName.text = item.fcltynm
                ivHomeUpcomingShowPoster.setOnClickListener {
                    item.mt20id?.let { it1 -> listener?.posterClicked(it1) }
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