package com.nbc.curtaincall.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nbc.curtaincall.databinding.ConstraintLayoutSearchRvBinding
import com.nbc.curtaincall.search.model.SearchItem
import com.nbc.curtaincall.ui.home.adapter.PosterClickListener

class SearchListAdapter(private val listener: PosterClickListener? = null) :
    ListAdapter<SearchItem, SearchListAdapter.SearchShowViewHolder>(searchCallback) {

    companion object {
        private val searchCallback = object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
                return oldItem.mt20id == newItem.mt20id
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
    }

    inner class  SearchShowViewHolder(private val binding:ConstraintLayoutSearchRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : SearchItem) {
            with(binding) {
//                Glide.with(itemView).load(item.poster).into(ivSearchResult)
                tvSearchitemGenre.setText(item.genrenm)
                tvSearchitemPrfstate.setText(item.prfstate)
                ivSearchResult.load(item.poster)
                ivSearchResult.setOnClickListener {
                    item.mt20id?.let { id ->
                        listener?.posterClicked(id) // 클릭시 해당 id 전달하여 간단화면 띄우기
                    }
                }
            }
        }
    }
}