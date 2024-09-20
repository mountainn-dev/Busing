package com.san.busing.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemRouteSearchResultBinding
import com.san.busing.domain.modelimpl.RouteModels
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener

class RouteSearchResultAdapter(
    private val items: RouteModels,
    private val itemClickEventListener: ItemClickEventListener,
    private val activity: Activity
) : RecyclerView.Adapter<RouteSearchResultAdapter.RouteSearchResultViewHolder>() {
    inner class RouteSearchResultViewHolder(
        private val binding: ItemRouteSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setContentColor(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtRouteName.text = items.get(position).name
            binding.txtRouteTypeTag.text = items.get(position).type.tag
            binding.txtRegion.text = items.get(position).regionName
        }

        private fun setContentColor(position: Int) {
            val color = ContextCompat.getColor(
                activity, Utils.getColorByRouteType(items.get(position).type))

            binding.txtRouteName.setTextColor(color)
            binding.txtRouteTypeTag.setTextColor(color)
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clRouteSearchResultItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RouteSearchResultViewHolder {
        val binding = ItemRouteSearchResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RouteSearchResultViewHolder(binding)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: RouteSearchResultViewHolder, position: Int) {
        holder.bind(position)
    }
}