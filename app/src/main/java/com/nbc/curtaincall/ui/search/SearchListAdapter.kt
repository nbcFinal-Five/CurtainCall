package com.nbc.curtaincall.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nbc.curtaincall.data.model.SearchItem
import com.nbc.curtaincall.databinding.ConstraintLayoutSearchRvBinding

class SearchListAdapter : ListAdapter<SearchItem, SearchListAdapter.SearchShowViewHolder>(searchCallback) {
    interface ItemCLick {
        fun onClick (position: Int)
    }
    var itemClick : ItemCLick? = null

    companion object {
        private val searchCallback = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem:SearchItem): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchShowViewHolder {
        return SearchShowViewHolder(ConstraintLayoutSearchRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SearchShowViewHolder, position: Int) {
        val showItem = getItem(position)
        holder.bind(showItem)
        holder.itemView.setOnClickListener {
            itemClick?.onClick(position)
        }
    }

    inner class  SearchShowViewHolder(private val binding:ConstraintLayoutSearchRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : SearchItem) {
            with(binding) {
                Glide.with(itemView).load(item.poster).into(ivSearchResult)
            }
        }
    }
}