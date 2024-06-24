package com.san.busing.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.R
import com.san.busing.databinding.ItemRouteStationBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener

class RouteStationAdapter(
    private val routeType: RouteType,
    private val stationItems: List<RouteStationModel>,
    private val busItems: List<BusModel>,
    private val itemClickEventListener: ItemClickEventListener,
    private val context: Activity
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
            loadBusInfoBy(position)
            loadTurnaroundBy(position)
        }

        private fun loadBusInfoBy(position: Int) {
            val busIdx = getBusIndex(position)

            if (busIdx != NO_MATCH_BUS) {   // 정류소 순번과 일치하는 버스가 존재하는 경우
                loadBusInfo(busItems[busIdx])
            } else { unloadBusInfo() }
        }

        private fun getBusIndex(position: Int): Int {
            var idx = NO_MATCH_BUS

            for (i in busItems.indices) {
                if (busItems[i].sequenceNumber == position+1) idx = i
            }

            return idx
        }

        private fun loadBusInfo(item: BusModel) {
            binding.llBusInfo.visibility = View.VISIBLE
            binding.lineBusInfo.visibility = View.VISIBLE
            binding.imgBus.visibility = View.VISIBLE
            binding.txtPlateNumber.text = item.plateNumber
            binding.txtRemainSeat.text = remainSeatText(item.remainSeat)
        }

        private fun remainSeatText(count: Int): String {
            if (count == Const.NO_DATA) return NO_REMAIN_SEAT_COUNT
            else return String.format(REMAIN_SEAT_COUNT, count)
        }

        private fun unloadBusInfo() {
            binding.llBusInfo.visibility = View.GONE
            binding.lineBusInfo.visibility = View.GONE
            binding.imgBus.visibility = View.GONE
        }

        private fun loadTurnaroundBy(position: Int) {
            if (stationItems[position].isTurnaround) loadTurnaround()
            else unloadTurnaround()
        }

        private fun loadTurnaround() {
            binding.imgWay.setImageResource(R.drawable.ic_turnaround)
        }

        private fun unloadTurnaround() {
            binding.imgWay.setImageResource(R.drawable.ic_down_arrow)
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

    companion object {
        private const val NO_MATCH_BUS = -1
        private const val REMAIN_SEAT_COUNT = "%d석"
        private const val NO_REMAIN_SEAT_COUNT = "-석"
    }
}