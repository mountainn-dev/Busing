package com.san.busing.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemRouteRecentSearchBinding
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener

class RouteRecentSearchAdapter(
    private val items: List<RouteRecentSearchModel>,
    private val itemClickEventListener: ItemClickEventListener,
    private val context: Activity
) : RecyclerView.Adapter<RouteRecentSearchAdapter.BusRouteRecentSearchViewHolder>() {

    inner class BusRouteRecentSearchViewHolder(
        private val binding: ItemRouteRecentSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            loadContent(position)
            setBackground(position)
            setContentColor(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtBusRouteRecentSearchName.text = items[position].name
        }

        private fun setBackground(position: Int) {
            val background = ContextCompat.getDrawable(context, Utils.getBackgroundByBookMarkStatus(items[position].bookMark))

            binding.clRouteRecentSearchItem.background = background
        }

        private fun setContentColor(position: Int) {
            val color = ContextCompat.getColor(context, Utils.getColorByRouteType(items[position].type))

            binding.txtBusRouteRecentSearchName.setTextColor(color)
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clRouteRecentSearchItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position) }
            binding.btnDeleteRecentSearch.setOnClickListener {
                itemClickEventListener.onDeleteButtonClickListener(position) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BusRouteRecentSearchViewHolder {
        val binding = ItemRouteRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return BusRouteRecentSearchViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BusRouteRecentSearchViewHolder, position: Int) {
        holder.bind(position)
    }
}