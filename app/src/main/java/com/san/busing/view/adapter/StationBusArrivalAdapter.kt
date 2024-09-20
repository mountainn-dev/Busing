package com.san.busing.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemStationBusArrivalBinding
import com.san.busing.domain.modelimpl.BusArrivalModel
import com.san.busing.domain.modelimpl.RouteModels
import com.san.busing.domain.modelimpl.RouteStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils

class StationBusArrivalAdapter(
    private val routeItems: RouteModels,
    private val nextStations: List<RouteStationModel>,
    private val busArrivals: List<BusArrivalModel>,
    private val activity: Activity
) : RecyclerView.Adapter<StationBusArrivalAdapter.StationBusArrivalViewHolder>(){
    inner class StationBusArrivalViewHolder(
        private val binding: ItemStationBusArrivalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setContentColor(position)
        }

        private fun loadContent(position: Int) {
            loadViaRouteInfo(position)
            loadNextStation(position)
            loadBusArrival(position)
        }

        private fun loadViaRouteInfo(position: Int) {
            binding.txtRouteName.text = routeItems.get(position).name
        }

        private fun loadNextStation(position: Int) {
            val nextStationName = nextStations.find { routeItems.get(position).isSame(it.getViaRouteId()) }?.name

            if (nextStationName != null) loadNextStation(nextStationName)
            else unloadNextStation()
        }

        private fun loadNextStation(name: String) {
            binding.txtNextStationName.text = name
        }

        private fun unloadNextStation() {
            binding.txtNextStationName.text = Const.EMPTY_TEXT
        }

        private fun loadBusArrival(position: Int) {
            val busArrival = busArrivals.find { routeItems.get(position).isSame(it.id) }

            if (busArrival != null) loadBusArrival(busArrival)
            else unloadBusArrival()
        }

        private fun loadBusArrival(busArrival: BusArrivalModel) {
            if (busArrival.predictTimeFirst == Const.ZERO) unloadBusArrivalFirst()
            else {
                binding.txtPredictTimeFirst.text = busArrivalPredictTimeMessage(busArrival.predictTimeFirst)
                binding.txtLocationFirst.text = busArrivalLocationMessage(busArrival.locationFirst)
            }
            if (busArrival.predictTimeSecond == Const.ZERO) unloadBusArrivalSecond()
            else {
                binding.txtPredictTimeSecond.text = busArrivalPredictTimeMessage(busArrival.predictTimeSecond)
                binding.txtLocationSecond.text = busArrivalLocationMessage(busArrival.locationSecond)
            }
        }

        private fun unloadBusArrival() {
            unloadBusArrivalFirst()
            unloadBusArrivalSecond()
        }

        private fun unloadBusArrivalFirst() {
            binding.llArrivalFirst.visibility = View.GONE
            binding.txtNoArrivalFirst.visibility = View.VISIBLE
        }

        private fun unloadBusArrivalSecond() {
            binding.llArrivalSecond.visibility = View.GONE
            binding.txtNoArrivalSecond.visibility = View.VISIBLE
        }

        private fun setContentColor(position: Int) {
            val color = ContextCompat.getColor(activity, Utils.getColorByRouteType(routeItems.get(position).type))

            binding.txtRouteName.setTextColor(color)
        }

        private fun busArrivalPredictTimeMessage(time: Int) = String.format(REMAIN_BUS_ARRIVAL_TIME, time)
        private fun busArrivalLocationMessage(location: Int) = String.format(REMAIN_BUS_ARRIVAL_LOCATION, location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationBusArrivalViewHolder {
        val binding = ItemStationBusArrivalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        initAnimEffect(binding)

        return StationBusArrivalViewHolder((binding))
    }

    private fun initAnimEffect(binding: ItemStationBusArrivalBinding) {
        initEllipsizeMarqueeEffect(binding)
    }

    private fun initEllipsizeMarqueeEffect(binding: ItemStationBusArrivalBinding) {
        binding.txtNextStationName.setHorizontallyScrolling(true)
        binding.txtNextStationName.isSelected = true
    }

    override fun getItemCount() = routeItems.count()

    override fun onBindViewHolder(holder: StationBusArrivalViewHolder, position: Int) {
        holder.bind(position)
    }

    companion object {
        private const val REMAIN_BUS_ARRIVAL_TIME = "%d분"
        private const val REMAIN_BUS_ARRIVAL_LOCATION = "%d번째 전"
    }
}