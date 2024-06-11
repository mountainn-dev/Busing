package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemBusRouteStationBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.BusStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener
import java.util.Queue

class BusRouteStationAdapter(
    private val routeType: RouteType,
    private val stationItems: List<BusStationModel>,
    private val busItems: Queue<BusModel>,
    private val itemClickEventListener: ItemClickEventListener,
) : RecyclerView.Adapter<BusRouteStationAdapter.BusRouteStationViewHolder>() {
    inner class BusRouteStationViewHolder(
        private val binding: ItemBusRouteStationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtRouteStationName.text = stationItems[position].name
            if (!busItems.isEmpty() && busItems.peek().sequenceNumber == (position+1)) {
                val item = busItems.poll()!!
                binding.llBusInfo.visibility = View.VISIBLE
                binding.txtPlateNumber.text = item.plateNumber
                binding.txtRemainSeat.text =item.remainSeat.toString()
                binding.imgBus.visibility = View.VISIBLE
            } else {
                binding.txtPlateNumber.text = Const.EMPTY_TEXT
                binding.txtRemainSeat.text = Const.EMPTY_TEXT
            }
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clRouteStationItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteStationViewHolder {
        val binding = ItemBusRouteStationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        setContentColor(binding, routeType)
        return BusRouteStationViewHolder(binding)
    }

    private fun setContentColor(binding: ItemBusRouteStationBinding, type: RouteType) {
        binding.imgBus.setImageResource(
            Utils.getBusImageResourceByRouteType(type)
        )
    }

    override fun getItemCount() = stationItems.size

    override fun onBindViewHolder(holder: BusRouteStationViewHolder, position: Int) {
        holder.bind(position)
    }
}