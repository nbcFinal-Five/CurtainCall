package com.nbc.shownect.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbc.shownect.databinding.ItemTopRankBinding
import com.nbc.shownect.fetch.model.BoxofResponse

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
                tvHotRecommendGenre.text = item.cate
                tvHotRecommendPeriod.text = item.prfpd?.substring(10, 21)
                tvHotRecommendPlaceName.text = item.prfplcnm
                tvHotRecommendPerformanceName.text = item.prfnm
                ivHomeHotRecommend.load("http://www.kopis.or.kr${item.poster}")
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
