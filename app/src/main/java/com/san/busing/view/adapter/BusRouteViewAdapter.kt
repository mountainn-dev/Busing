package com.san.busing.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.view.screen.BusRouteDetailActivity
import com.san.busing.databinding.ItemBusRouteBinding
import com.san.busing.domain.model.BusRouteModel

class BusRouteViewAdapter(private val items: List<BusRouteModel>, private val context: Context) :
    RecyclerView.Adapter<BusRouteViewAdapter.BusRouteViewHolder>() {
    inner class BusRouteViewHolder(private val binding: ItemBusRouteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            bindContent(position)
            setOnItemClickListener(position)
        }

        private fun bindContent(position: Int) {
            binding.txtRouteName.text = items[position].name
            binding.txtRegion.text = items[position].region
        }

        private fun setOnItemClickListener(position: Int) {
            binding.clRouteItem.setOnClickListener {
                val intent = Intent(context, BusRouteDetailActivity::class.java)
                intent.putExtra("routeId", items[position].id)

                context.startActivity(intent)
            }
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