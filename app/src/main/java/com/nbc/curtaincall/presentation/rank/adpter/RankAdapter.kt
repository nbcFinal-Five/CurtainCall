package com.nbc.curtaincall.presentation.rank.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.data.model.BoxofResponse
import com.nbc.curtaincall.databinding.ItemRankBinding
import com.nbc.curtaincall.ui.home.adapter.PosterClickListener

class RankAdapter(private val listener: PosterClickListener? = null) :
    androidx.recyclerview.widget.ListAdapter<BoxofResponse, RankAdapter.RankViewHolder>(object :
        DiffUtil.ItemCallback<BoxofResponse>() {
        override fun areItemsTheSame(oldItem: BoxofResponse, newItem: BoxofResponse): Boolean {
            return oldItem.rnum == newItem.rnum
        }

        override fun areContentsTheSame(oldItem: BoxofResponse, newItem: BoxofResponse): Boolean {
            return oldItem == newItem
        }

    }) {
    inner class RankViewHolder(private val binding: ItemRankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BoxofResponse) {
            with(binding) {
                Glide.with(itemView).load("http://www.kopis.or.kr${item.poster}").into(ivRankPoster)
                tvRank.text = item.rnum.toString()
                tvTitle.text = item.prfnm
                tvPlace.text = item.prfplcnm
                tvPeriod.text = item.prfpd
                ivRankPoster.setOnClickListener {
                    listener?.posterClicked(item.mt20id.toString())
                }
                tvTitle.setOnClickListener {
                    listener?.posterClicked(item.mt20id.toString())
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