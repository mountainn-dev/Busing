package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemRecentSearchBusRouteBinding
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.view.listener.ItemClickEventListener

class BusRouteRecentSearchAdapter(
    private val items: List<BusRouteRecentSearchModel>,
    private val itemClickEventListener: ItemClickEventListener
) : RecyclerView.Adapter<BusRouteRecentSearchAdapter.BusRouteRecentSearchViewHolder>() {

    inner class BusRouteRecentSearchViewHolder(
        private val binding: ItemRecentSearchBusRouteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            loadContent(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtBusRouteRecentSearchName.text = items[position].name
            binding.idx.text = items[position].index.toString()
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clBusRouteRecentSearch.setOnClickListener {
                itemClickEventListener.onItemClickListener(position) }
            binding.btnDelete.setOnClickListener {
                itemClickEventListener.onDeleteButtonClickListener(position) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BusRouteRecentSearchViewHolder {
        val binding = ItemRecentSearchBusRouteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return BusRouteRecentSearchViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BusRouteRecentSearchViewHolder, position: Int) {
        holder.bind(position)
    }
}