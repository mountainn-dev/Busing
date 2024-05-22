package com.san.busing.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.data.entity.Test
import com.san.busing.databinding.ItemRecentSearchBusRouteBinding
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.utils.Const
import com.san.busing.view.screen.BusRouteDetailActivity

class BusRouteRecentSearchAdapter(
    // TODO: test 엔티티 전환
    private val items: List<Test>, private val context: Context
) : RecyclerView.Adapter<BusRouteRecentSearchAdapter.BusRouteRecentSearchViewHolder>() {
    inner class BusRouteRecentSearchViewHolder(
        private val binding: ItemRecentSearchBusRouteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            bindContent(position)
            setOnItemClickListener(position)
        }

        private fun bindContent(position: Int) {
            binding.txtRecentSearchRouteName.text = items[position].name
        }

        private fun setOnItemClickListener(position: Int) {
            binding.clRecentSearchBusRoute.setOnClickListener {
                val intent = Intent(context, BusRouteDetailActivity::class.java)
                intent.putExtra(Const.TAG_ROUTE_ID, items[position].name)

                context.startActivity(intent)
            }
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