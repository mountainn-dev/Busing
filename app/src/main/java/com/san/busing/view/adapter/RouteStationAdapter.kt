package com.san.busing.view.adapter

import android.app.Activity
import android.util.Log
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
import java.util.Queue

class RouteStationAdapter(
    private val routeType: RouteType,
    private val stationItems: List<RouteStationModel>,
    private val busItems: Queue<BusModel>,
    private val itemClickEventListener: ItemClickEventListener,
    private val context: Activity
) : RecyclerView.Adapter<RouteStationAdapter.BusRouteStationViewHolder>() {
    inner class BusRouteStationViewHolder(
        private val binding: ItemRouteStationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setTurnaround(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            binding.txtRouteStationName.text = stationItems[position].name
            binding.txtRouteStationNumber.text = stationItems[position].number
            if (busItems.peek()?.sequenceNumber == (position+1)) {   // 버스 위치와 정류소 순번이 일치하는 경우
                loadBusInfo(busItems.poll()!!)
            } else { unloadBusInfo() }
        }

        private fun loadBusInfo(item: BusModel) {
            Log.d("busItem", item.sequenceNumber.toString())
            binding.llBusInfo.visibility = View.VISIBLE
            binding.lineBusInfo.visibility = View.VISIBLE
            binding.imgBus.visibility = View.VISIBLE
            binding.txtPlateNumber.text = item.plateNumber
            binding.txtRemainSeat.text = remainSeatText(item.remainSeat)

            // 중복 차량 처리
            while (busItems.peek()?.sequenceNumber == item.sequenceNumber) busItems.poll()
        }

        private fun remainSeatText(count: Int): String {
            if (count == Const.NO_DATA) return Const.NO_REMAIN_SEAT_COUNT
            else return String.format(Const.REMAIN_SEAT_COUNT, count)
        }

        private fun unloadBusInfo() {
            binding.llBusInfo.visibility = View.GONE
            binding.lineBusInfo.visibility = View.GONE
        }

        private fun setTurnaround(position: Int) {
            if (stationItems[position].isTurnaround) {   // 회차지인 경우
                binding.clRouteStationItem.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.semi_light_grey))
                binding.imgWay.setImageResource(R.drawable.ic_turnaround)
            }
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