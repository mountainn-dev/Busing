package com.san.busing.view.adapter.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.R
import com.san.busing.databinding.ItemRouteStationBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.Passable
import com.san.busing.domain.modelimpl.route.BusModel
import com.san.busing.domain.modelimpl.route.BusModels
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener

class RouteStationAdapter(
    private val routeType: RouteType,
    private val stationItems: StationModels,
    private val busItems: BusModels,
    private val itemClickEventListener: ItemClickEventListener,
) : RecyclerView.Adapter<RouteStationAdapter.RouteStationViewHolder>() {
    inner class RouteStationViewHolder(
        private val binding: ItemRouteStationBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setItemClickEventListener(position)
        }

        private fun loadContent(position: Int) {
            loadStationInfo(position)
            loadBusInfo(position)
            loadTurnaround(position)
        }

        private fun loadStationInfo(position: Int) {
            binding.txtRouteStationName.text = stationItems.get(position).name
            binding.txtRouteStationNumber.text = stationItems.get(position).mobileNo
        }

        private fun loadBusInfo(position: Int) {
            val bus = busItems.getOrNullBySeq(position + 1) // sequence = index + 1

            if (bus != null) {
                loadBusInfo(bus)
            } else {
                unloadBusInfo()
            }
        }

        private fun loadBusInfo(item: BusModel) {
            binding.llBusInfo.visibility = View.VISIBLE
            binding.imgBus.visibility = View.VISIBLE
            binding.txtPlateNumber.text = item.plateNumber
            binding.txtRemainSeat.text = remainSeatText(item.remainSeat)
        }

        private fun remainSeatText(count: Int) =
            if (count != Const.NO_DATA) {
                String.format(REMAIN_SEAT_COUNT, count)
            } else {
                NO_REMAIN_SEAT_COUNT
            }

        private fun unloadBusInfo() {
            binding.llBusInfo.visibility = View.GONE
            binding.imgBus.visibility = View.GONE
        }

        private fun loadTurnaround(position: Int) {
            if ((stationItems.get(position) as Passable).isTurnaround) {
                loadTurnaround()
            } else {
                unloadTurnaround()
            }
        }

        private fun loadTurnaround() {
            binding.imgWay.setImageResource(R.drawable.ic_turnaround)
        }

        private fun unloadTurnaround() {
            binding.imgWay.setImageResource(R.drawable.ic_down_way)
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clRouteStationItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RouteStationViewHolder {
        val binding =
            ItemRouteStationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        setContentColor(binding, routeType)
        return RouteStationViewHolder(binding)
    }

    private fun setContentColor(
        binding: ItemRouteStationBinding,
        type: RouteType,
    ) {
        binding.imgBus.setImageResource(
            Utils.getBusImageResourceByRouteType(type),
        )
    }

    override fun getItemCount() = stationItems.count()

    override fun onBindViewHolder(
        holder: RouteStationViewHolder,
        position: Int,
    ) {
        holder.bind(position)
    }

    companion object {
        private const val REMAIN_SEAT_COUNT = "%d석"
        private const val NO_REMAIN_SEAT_COUNT = "-석"
    }
}
