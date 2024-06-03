package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemBusRouteStationBinding
import com.san.busing.domain.model.BusStationModel
import com.san.busing.view.listener.ItemClickEventListener

class BusRouteStationAdapter(
    private val items: List<BusStationModel>,
    private val itemClickEventListener: ItemClickEventListener,
) : RecyclerView.Adapter<BusRouteStationAdapter.BusRouteStationViewHolder>() {
    inner class BusRouteStationViewHolder(
        private val binding: ItemBusRouteStationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtRouteStationName.text = items[position].name
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clRouteStationItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteStationViewHolder {
        val binding = ItemBusRouteStationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BusRouteStationViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BusRouteStationViewHolder, position: Int) {
        holder.bind(position)
    }
}