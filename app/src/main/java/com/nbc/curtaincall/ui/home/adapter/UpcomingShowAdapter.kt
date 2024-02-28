package com.nbc.curtaincall.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nbc.curtaincall.data.model.Db
import com.nbc.curtaincall.databinding.ItemUpcomingShowBinding
import com.nbc.curtaincall.ui.mypage.SimpleInfoBottomSheetFragment

class UpcomingShowAdapter(private val items: List<Db>) :
    RecyclerView.Adapter<UpcomingShowAdapter.UpcomingShowViewHolder>() {
    inner class UpcomingShowViewHolder(private val binding: ItemUpcomingShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Db) {
            with(binding) {
                ivHomeUpcomingShowPoster.load(item.poster)
                tvPerformanceName.text = item.prfnm
                tvPeriod.text = "${item.prfpdfrom} ~ ${item.prfpdto}"
                tvFacilityName.text = item.fcltynm
                tvPageIndicator.text = "${adapterPosition + 1} / 10"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingShowViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemUpcomingShowBinding.inflate(inflater, parent, false)
        return UpcomingShowViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UpcomingShowViewHolder, position: Int) {
        holder.bind(items[position])
    }


}