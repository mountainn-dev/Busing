package com.san.busing.view.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.view.screen.BusRouteDetailActivity
import com.san.busing.databinding.ItemSearchResultBusRouteBinding
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.utils.Const

class BusRouteSearchResultAdapter(
    private val items: List<BusRouteModel>, private val context: Context
) : RecyclerView.Adapter<BusRouteSearchResultAdapter.BusRouteSearchResultViewHolder>() {
    inner class BusRouteSearchResultViewHolder(
        private val binding: ItemSearchResultBusRouteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
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
                intent.putExtra(
                    Const.TAG_ROUTE_ID,
                    BusRouteRecentSearchModel(items[position].id, items[position].name))

                context.startActivity(intent)
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