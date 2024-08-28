package com.nbc.curtaincall.presentation.rank.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.ItemRankBinding
import com.nbc.curtaincall.presentation.model.ShowItem

class RankAdapter(private val onClick: (String) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<ShowItem.TopRankItem, RankAdapter.RankViewHolder>(
        object :
            DiffUtil.ItemCallback<ShowItem.TopRankItem>() {
            override fun areItemsTheSame(
                oldItem: ShowItem.TopRankItem,
                newItem: ShowItem.TopRankItem
            ): Boolean {
                return oldItem.rankNum == newItem.rankNum
            }

            override fun areContentsTheSame(
                oldItem: ShowItem.TopRankItem,
                newItem: ShowItem.TopRankItem
            ): Boolean {
                return oldItem == newItem
            }

        }) {
    inner class RankViewHolder(private val binding: ItemRankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem.TopRankItem) {
            with(binding) {
                Glide.with(itemView).load("http://www.kopis.or.kr${item.posterPath}")
                    .into(ivRankPoster)
                tvRank.text = item.rankNum.toString()
                tvTitle.text = item.title
                tvPlace.text = item.placeName
                tvPeriod.text = item.period
                ivRankPoster.setOnClickListener {
                    item.showId?.let { id -> onClick(id) }
                }
                tvTitle.setOnClickListener {
                    item.showId?.let { id -> onClick(id) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemRankBinding.inflate(inflater, parent, false)
        return RankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}