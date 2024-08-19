package com.nbc.curtaincall.presentation.ticket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbc.curtaincall.databinding.ItemReserveInfoBinding
import com.nbc.curtaincall.presentation.model.ShowItem

class ListAdapter :
    ListAdapter<ShowItem.Relate, com.nbc.curtaincall.presentation.ticket.ListAdapter.ReserveViewHolder>(
        diffUtil
    ) {
    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ShowItem.Relate>() {
            override fun areItemsTheSame(
                oldItem: ShowItem.Relate,
                newItem: ShowItem.Relate
            ): Boolean = oldItem.relateUrl == newItem.relateUrl


            override fun areContentsTheSame(
                oldItem: ShowItem.Relate,
                newItem: ShowItem.Relate
            ): Boolean = oldItem == newItem
        }
    }

    class ReserveViewHolder(private val binding: ItemReserveInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem.Relate) {
            with(binding) {
                tvReserveName.text = item.relateName
                tvReserveUrl.text = item.relateUrl
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReserveViewHolder {
        return ReserveViewHolder(
            ItemReserveInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReserveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}