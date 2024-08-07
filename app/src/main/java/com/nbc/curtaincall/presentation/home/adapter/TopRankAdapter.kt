package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.databinding.ItemTopRankBinding
import com.nbc.curtaincall.presentation.home.adapter.DiffCallback
import com.nbc.curtaincall.presentation.model.ShowItem

class TopRankAdapter(private val listener: PosterClickListener? = null) :
    ListAdapter<ShowItem, TopRankAdapter.TopRankViewHolder>(DiffCallback()) {

    inner class TopRankViewHolder(private val binding: ItemTopRankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShowItem) {
            item as ShowItem.TopRankItem
            with(binding) {
                Glide.with(itemView).load("http://www.kopis.or.kr${item.posterPath}")
                    .into(ivTopRankPoster)
                tvPeriod.text = item.period
                tvFacilityName.text = item.placeName
                tvPerformanceName.text = item.title
                ivTopRankPoster.setOnClickListener {
                    item.showId?.let { id ->
                        listener?.posterClicked(
                            id
                        )
                    }
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
