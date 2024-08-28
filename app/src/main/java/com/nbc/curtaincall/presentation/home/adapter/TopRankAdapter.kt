package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nbc.curtaincall.databinding.ItemTopRankBinding
import com.nbc.curtaincall.presentation.home.adapter.DiffCallback
import com.nbc.curtaincall.presentation.model.ShowItem
import com.nbc.curtaincall.util.ViewUtil.setPoster

class TopRankAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<ShowItem, TopRankAdapter.TopRankViewHolder>(DiffCallback()) {

    inner class TopRankViewHolder(private val binding: ItemTopRankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem) {
            item as ShowItem.TopRankItem
            with(binding) {
                ivTopRankPoster.setPoster(itemView, "http://www.kopis.or.kr${item.posterPath}")
                tvPeriod.text = item.period
                tvFacilityName.text = item.placeName
                tvPerformanceName.text = item.title
                ivTopRankPoster.setOnClickListener {
                    item.showId?.let { id -> onClick(id) }
                }
            }
        }
    }

    override fun getItemCount(): Int = currentList.size * currentList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRankViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemTopRankBinding.inflate(inflater, parent, false)
        return TopRankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopRankViewHolder, position: Int) {
        holder.bind(currentList[position % currentList.size])
    }
}
