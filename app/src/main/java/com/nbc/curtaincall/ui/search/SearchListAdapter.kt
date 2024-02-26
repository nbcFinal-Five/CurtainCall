package com.nbc.curtaincall.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.data.model.ShowListResponse
import com.nbc.curtaincall.databinding.ConstraintLayoutSearchRvBinding

class SearchListAdapter : ListAdapter<ShowListResponse, SearchListAdapter.ChargerViewHolder>(searchCallback) {
    interface ItemCLick {
        fun onClick (position: Int)
    }
    var itemClick : ItemCLick? = null

    companion object {
        private val searchCallback = object : DiffUtil.ItemCallback<ShowListResponse>() {
            override fun areItemsTheSame(oldItem: ShowListResponse, newItem:ShowListResponse): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: ShowListResponse, newItem: ShowListResponse): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargerViewHolder {
        return ChargerViewHolder(ConstraintLayoutSearchRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ChargerViewHolder, position: Int) {
        val showItem = getItem(position)
        holder.bind(showItem)
        holder.itemView.setOnClickListener {
            itemClick?.onClick(position)
        }
    }

    inner class  ChargerViewHolder(private val binding:ConstraintLayoutSearchRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ShowListResponse) {
            with(binding) {
                Glide.with(itemView).load(item.showList).into(ivSearchResult)
            }
        }
    }
}