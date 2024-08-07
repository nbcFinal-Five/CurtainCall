package com.nbc.curtaincall.presentation.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.nbc.curtaincall.presentation.model.ShowItem

class DiffCallback<T : ShowItem> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.showId == newItem.showId

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem
}