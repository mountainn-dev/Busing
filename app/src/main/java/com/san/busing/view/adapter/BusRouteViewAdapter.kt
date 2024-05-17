package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemBusRouteBinding
import com.san.busing.domain.model.BusRouteModel

class BusRouteViewAdapter(private val items: List<BusRouteModel>) : RecyclerView.Adapter<BusRouteViewAdapter.BusRouteViewHolder>() {
    inner class BusRouteViewHolder(private val binding: ItemBusRouteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.txtRouteName.text = items[position].name
            binding.txtRegion.text = items[position].region
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteViewHolder {
        val binding = ItemBusRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusRouteViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BusRouteViewHolder, position: Int) {
        holder.bind(position)
    }
}