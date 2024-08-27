package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemStationSearchResultBinding
import com.san.busing.domain.model.StationSummaryModel
import com.san.busing.domain.model.StationSummaryModels
import com.san.busing.view.listener.ItemClickEventListener

class StationSearchResultAdapter(
    private val items: StationSummaryModels,
    private val itemClickEventListener: ItemClickEventListener
) : RecyclerView.Adapter<StationSearchResultAdapter.StationSearchResultViewHolder>() {
    inner class StationSearchResultViewHolder(
        private val binding: ItemStationSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtStationName.text = items.get(position).name
            binding.txtStationMobileNo.text = items.get(position).mobileNo
            binding.txtRegionName.text = items.get(position).regionName
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clStationSearchResultItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StationSearchResultViewHolder {
        val binding = ItemStationSearchResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return StationSearchResultViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: StationSearchResultViewHolder, position: Int) {
        holder.bind(position)
    }
}