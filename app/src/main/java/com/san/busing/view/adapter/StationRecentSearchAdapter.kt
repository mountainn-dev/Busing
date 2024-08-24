package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemStationRecentSearchBinding
import com.san.busing.domain.model.StationRecentSearchModel
import com.san.busing.view.listener.ItemClickEventListener

class StationRecentSearchAdapter(
    private val items: List<StationRecentSearchModel>,
    private val itemClickEventListener: ItemClickEventListener
) : RecyclerView.Adapter<StationRecentSearchAdapter.StationRecentSearchViewHolder>() {
    inner class StationRecentSearchViewHolder(
        private val binding: ItemStationRecentSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtStationRecentSearchName.text = items[position].name
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clStationRecentSearchItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position) }
            binding.btnDeleteRecentSearch.setOnClickListener {
                itemClickEventListener.onDeleteButtonClickListener(position) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StationRecentSearchAdapter.StationRecentSearchViewHolder {
        val binding = ItemStationRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return StationRecentSearchViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(
        holder: StationRecentSearchAdapter.StationRecentSearchViewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }
}