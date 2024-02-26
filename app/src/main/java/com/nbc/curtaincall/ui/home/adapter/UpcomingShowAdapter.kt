package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nbc.curtaincall.data.model.ShowItem
import com.nbc.curtaincall.databinding.ItemBeforeShowBinding

class UpcomingShowAdapter(private val showList: List<ShowItem>) :
    RecyclerView.Adapter<UpcomingShowAdapter.BeforeShowViewHolder>() {
    inner class BeforeShowViewHolder(private val binding: ItemBeforeShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(showItem: ShowItem) {
            with(binding) {
                ivHomeBeforeShowPoster.load(showItem.poster)
                tvPerformanceName.text = showItem.prfnm
                tvPeriod.text = "${showItem.prfpdfrom} ~ ${showItem.prfpdto}"
                tvFacilityName.text = showItem.fcltynm
                tvPageIndicator.text = "${adapterPosition + 1} / 10"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeforeShowViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemBeforeShowBinding.inflate(inflater, parent, false)
        return BeforeShowViewHolder(binding)
    }

    override fun getItemCount(): Int = showList.size

    override fun onBindViewHolder(holder: BeforeShowViewHolder, position: Int) {
        holder.bind(showList[position])
    }
}