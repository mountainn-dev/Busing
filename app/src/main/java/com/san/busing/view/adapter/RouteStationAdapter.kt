package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemRouteStationBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener
import java.util.Queue

class RouteStationAdapter(
    private val routeType: RouteType,
    private val stationItems: List<RouteStationModel>,
    private val busItems: Queue<BusModel>,
    private val itemClickEventListener: ItemClickEventListener,
) : RecyclerView.Adapter<RouteStationAdapter.BusRouteStationViewHolder>() {
    inner class BusRouteStationViewHolder(
        private val binding: ItemRouteStationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtRouteStationName.text = stationItems[position].name
            binding.txtRouteStationNumber.text = stationItems[position].number

            if (busItems.peek()?.sequenceNumber == (position+1)) {   // 버스 위치와 정류소 순번이 일치하는 경우
                val item = busItems.poll()!!
                binding.llBusInfo.visibility = View.VISIBLE
                binding.imgBus.visibility = View.VISIBLE
                binding.txtPlateNumber.text = item.plateNumber
                binding.txtRemainSeat.text = remainSeatText(item.remainSeat)
            } else {
                binding.txtPlateNumber.visibility = View.GONE
                binding.txtRemainSeat.visibility = View.GONE
            }
        }

        private fun remainSeatText(count: Int): String {
            if (count == Const.NO_DATA) return Const.NO_REMAIN_SEAT_COUNT
            else return String.format(Const.REMAIN_SEAT_COUNT, count)
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clRouteStationItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteStationViewHolder {
        val binding = ItemRouteStationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        setContentColor(binding, routeType)
        return BusRouteStationViewHolder(binding)
    }

    private fun setContentColor(binding: ItemRouteStationBinding, type: RouteType) {
        binding.imgBus.setImageResource(
            Utils.getBusImageResourceByRouteType(type)
        )
    }

    override fun getItemCount() = stationItems.size

    override fun onBindViewHolder(holder: BusRouteStationViewHolder, position: Int) {
        holder.bind(position)
    }
}