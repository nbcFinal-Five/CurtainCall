package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nbc.curtaincall.data.model.Boxof
import com.nbc.curtaincall.databinding.ItemTopRankBinding

class TopRankAdapter :
    RecyclerView.Adapter<TopRankAdapter.TopRankViewHolder>() {
    var items: List<Boxof> = listOf()

    inner class TopRankViewHolder(private val binding: ItemTopRankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Boxof) {
            with(binding) {
                tvHotRecommendGenre.text = item.cate
                tvHotRecommendPeriod.text = item.prfpd?.substring(10, 21)
                tvHotRecommendPlaceName.text = item.prfplcnm
                tvHotRecommendPerformanceName.text = item.prfnm
                ivHomeHotRecommend.load("http://www.kopis.or.kr${item.poster}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRankViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemTopRankBinding.inflate(inflater, parent, false)
        return TopRankViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TopRankViewHolder, position: Int) {
        holder.bind(items[position])
    }
}