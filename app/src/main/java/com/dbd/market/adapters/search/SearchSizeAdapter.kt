package com.dbd.market.adapters.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dbd.market.R

class SearchSizeAdapter(private val thisContext: Context): RecyclerView.Adapter<SearchSizeAdapter.SearchSizeViewHolder>() {

    inner class SearchSizeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val searchSizeTextView = itemView.findViewById<TextView>(R.id.searchSizeTextView)

        fun bind(size: String) { searchSizeTextView.text = size }
    }

    private val differCallback = object: DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSizeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_size_recycler_view_item_layout, parent, false)
        return SearchSizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchSizeViewHolder, position: Int) {
        val currentSize = differ.currentList[position]
        holder.bind(currentSize)
    }

    override fun getItemCount() = differ.currentList.size

}