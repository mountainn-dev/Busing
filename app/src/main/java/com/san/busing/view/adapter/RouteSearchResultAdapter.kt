package com.san.busing.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemSearchResultBusRouteBinding
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener

class RouteSearchResultAdapter(
    private val items: List<RouteSummaryModel>,
    private val itemClickEventListener: ItemClickEventListener,
    private val context: Context
) : RecyclerView.Adapter<RouteSearchResultAdapter.BusRouteSearchResultViewHolder>() {
    inner class BusRouteSearchResultViewHolder(
        private val binding: ItemSearchResultBusRouteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setContentColor(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtRouteName.text = items[position].name
            binding.txtRouteTypeTag.text = items[position].type.tag
            binding.txtRegion.text = items[position].region
        }

        private fun setContentColor(position: Int) {
            val color = ContextCompat.getColor(
                context, Utils.getColorByRouteType(items[position].type))

            binding.txtRouteName.setTextColor(color)
            binding.txtRouteTypeTag.setTextColor(color)
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clRouteItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BusRouteSearchResultViewHolder {
        val binding = ItemSearchResultBusRouteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BusRouteSearchResultViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BusRouteSearchResultViewHolder, position: Int) {
        holder.bind(position)
    }
}