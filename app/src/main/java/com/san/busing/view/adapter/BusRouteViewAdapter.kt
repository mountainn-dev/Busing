package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemBusRouteBinding
import com.san.busing.domain.model.BusRouteModel

class BusRouteViewAdapter(private val routes: List<BusRouteModel>) : RecyclerView.Adapter<BusRouteViewAdapter.BusRouteViewHolder>() {
    inner class BusRouteViewHolder(private val binding: ItemBusRouteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.txtId.text = routes[position].id.toString()
            binding.txtName.text = routes[position].name
            binding.txtTypeCd.text = routes[position].typeCd.toString()
            binding.txtTypeName.text = routes[position].typeName
            binding.txtRegion.text = routes[position].region
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteViewHolder {
        val binding = ItemBusRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusRouteViewHolder(binding)
    }

    override fun getItemCount() = routes.size

    override fun onBindViewHolder(holder: BusRouteViewHolder, position: Int) {
        holder.bind(position)
    }
}