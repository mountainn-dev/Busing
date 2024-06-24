package com.san.busing.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.R
import com.san.busing.databinding.ItemRouteRecentSearchBinding
import com.san.busing.domain.enums.RouteType.AIRPORT_LIMO
import com.san.busing.domain.enums.RouteType.AIRPORT_NORMAL
import com.san.busing.domain.enums.RouteType.AIRPORT_SEAT
import com.san.busing.domain.enums.RouteType.AREA_DIRECT
import com.san.busing.domain.enums.RouteType.AREA_EXPRESS
import com.san.busing.domain.enums.RouteType.CIRCULAR
import com.san.busing.domain.enums.RouteType.NORMAL
import com.san.busing.domain.enums.RouteType.NORMAL_SEAT
import com.san.busing.domain.enums.RouteType.OUT_TOWN_EXPRESS
import com.san.busing.domain.enums.RouteType.OUT_TOWN_NORMAL
import com.san.busing.domain.enums.RouteType.OUT_TOWN_SEAT
import com.san.busing.domain.enums.RouteType.RURAL_DIRECT
import com.san.busing.domain.enums.RouteType.RURAL_NORMAL
import com.san.busing.domain.enums.RouteType.RURAL_SEAT
import com.san.busing.domain.enums.RouteType.VILLAGE
import com.san.busing.domain.model.RouteRecentSearchModel
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
            setContentColor(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtBusRouteRecentSearchName.text = items[position].name
        }

        private fun setContentColor(position: Int) {
            val color = ContextCompat.getColor(context, colorIdByRouteType(position))

            binding.txtBusRouteRecentSearchName.setTextColor(color)
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