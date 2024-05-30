package com.san.busing.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.R
import com.san.busing.databinding.ItemRecentSearchBusRouteBinding
import com.san.busing.domain.enums.RouteType.*
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.view.listener.ItemClickEventListener

class BusRouteRecentSearchAdapter(
    private val items: List<BusRouteRecentSearchModel>,
    private val itemClickEventListener: ItemClickEventListener,
    private val context: Context
) : RecyclerView.Adapter<BusRouteRecentSearchAdapter.BusRouteRecentSearchViewHolder>() {

    inner class BusRouteRecentSearchViewHolder(
        private val binding: ItemRecentSearchBusRouteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            loadContent(position)
            setContentColor(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtBusRouteRecentSearchName.text = items[position].name
        }

        private fun setContentColor(position: Int) {
            binding.txtBusRouteRecentSearchName.setTextColor(
                ContextCompat.getColor(context, colorIdByRouteType(position))
            )
        }

        private fun colorIdByRouteType(position: Int): Int {
            return when (items[position].type) {
                AIRPORT_NORMAL, AIRPORT_LIMO, AIRPORT_SEAT, CIRCULAR -> R.color.deep_blue
                NORMAL, NORMAL_SEAT, OUT_TOWN_NORMAL, OUT_TOWN_EXPRESS, OUT_TOWN_SEAT -> R.color.blue
                AREA_EXPRESS, AREA_DIRECT -> R.color.red
                RURAL_NORMAL, RURAL_DIRECT, RURAL_SEAT, VILLAGE -> R.color.green
            }
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clBusRouteRecentSearch.setOnClickListener {
                itemClickEventListener.onItemClickListener(position) }
            binding.btnDeleteRecentSearch.setOnClickListener {
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