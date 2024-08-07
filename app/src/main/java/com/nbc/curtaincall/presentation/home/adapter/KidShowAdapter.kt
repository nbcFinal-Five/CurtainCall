package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.ItemKidShowBinding
import com.nbc.curtaincall.presentation.home.adapter.DiffCallback
import com.nbc.curtaincall.presentation.model.ShowItem

class KidShowAdapter(private val listener: PosterClickListener? = null) :
    ListAdapter<ShowItem, KidShowAdapter.KidShowViewHolder>(DiffCallback()) {

    inner class KidShowViewHolder(private val binding: ItemKidShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem) {
            with(binding) {
                item as ShowItem.KidShowItem
                Glide.with(itemView).load(item.posterPath).into(ivKidShow)
                tvKidPeriod.text = "~ ${item.periodTo}"
                tvKidGenre.text = item.genre
                tvKidShowingState.text = item.showingState
                tvKidPerformanceTitle.text = item.title
                tvKidShowPlaceName.text = item.placeName
                ivKidShow.setOnClickListener { item.showId?.let { id -> listener?.posterClicked(id) } }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KidShowViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemKidShowBinding.inflate(inflater, parent, false)
        return KidShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KidShowViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}