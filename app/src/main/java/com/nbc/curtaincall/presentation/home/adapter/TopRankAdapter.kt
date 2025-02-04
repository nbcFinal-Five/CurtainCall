package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.ItemTopRankBinding
import com.nbc.curtaincall.fetch.model.BoxofResponse

class TopRankAdapter(private val listener: PosterClickListener? = null) :
    ListAdapter<BoxofResponse, TopRankAdapter.TopRankViewHolder>(object :
        DiffUtil.ItemCallback<BoxofResponse>() {
        override fun areItemsTheSame(oldItem: BoxofResponse, newItem: BoxofResponse): Boolean {
            return oldItem.mt20id == newItem.mt20id
        }

        override fun areContentsTheSame(oldItem: BoxofResponse, newItem: BoxofResponse): Boolean {
            return oldItem == newItem
        }
    }) {

    inner class TopRankViewHolder(private val binding: ItemTopRankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BoxofResponse) {
            with(binding) {
                Glide.with(itemView).load("http://www.kopis.or.kr${item.poster}").into(ivHomeHotRecommend)
                tvHotRecommendGenre.text = item.cate
                tvHotRecommendPeriod.text = item.prfpd?.substring(10, 21)
                tvHotRecommendPlaceName.text = item.prfplcnm
                tvHotRecommendPerformanceName.text = item.prfnm
                ivHomeHotRecommend.setOnClickListener {
                    item.mt20id?.let { id ->
                        listener?.posterClicked(
                            id
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRankViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemTopRankBinding.inflate(inflater, parent, false)
        return TopRankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopRankViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}
