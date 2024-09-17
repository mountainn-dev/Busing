package com.san.busing.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemStationViaRouteBinding
import com.san.busing.domain.model.BusArrivalModel
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.model.StationViaRouteModels
import com.san.busing.domain.utils.Const

class StationViaRouteAdapter(
    private val routeItems: StationViaRouteModels,
    private val nextStations: List<RouteStationModel>,
    private val busArrivals: List<BusArrivalModel>
) : RecyclerView.Adapter<StationViaRouteAdapter.StationViaRouteViewHolder>(){
    inner class StationViaRouteViewHolder(
        private val binding: ItemStationViaRouteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
        }

        private fun loadContent(position: Int) {
            loadViaRouteInfo(position)
            loadNextStation(position)
            loadBusArrival(position)
        }

        private fun loadViaRouteInfo(position: Int) {
            binding.txtRouteName.text = routeItems.get(position).routeSummary.name
        }

        private fun loadNextStation(position: Int) {
            val nextStationName = nextStations.find { routeItems.get(position).isSame(it.id) }?.name

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

            binding.txtPredictTimeFirst.text = busArrivalPredictTimeMessage(busArrival?.predictTimeFirst)
            binding.txtPredictTimeSecond.text = busArrivalPredictTimeMessage(busArrival?.predictTimeSecond)
            binding.txtLocationFirst.text = busArrivalLocationMessage(busArrival?.locationFirst)
            binding.txtLocationSecond.text = busArrivalLocationMessage(busArrival?.locationSecond)
        }

        private fun busArrivalPredictTimeMessage(time: Int?) =
            if (time != null) String.format(REMAIN_BUS_ARRIVAL_TIME, time)
            else NO_REMAIN_BUS_ARRIVAL_TIME

        private fun busArrivalLocationMessage(location: Int?) =
            if (location != null) String.format(REMAIN_BUS_ARRIVAL_LOCATION, location)
            else NO_REMAIN_BUS_ARRIVAL_LOCATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViaRouteViewHolder {
        val binding = ItemStationViaRouteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return StationViaRouteViewHolder((binding))
    }

    override fun getItemCount() = routeItems.count()

    override fun onBindViewHolder(holder: StationViaRouteViewHolder, position: Int) {
        holder.bind(position)
    }

    companion object {
        private const val REMAIN_BUS_ARRIVAL_TIME = "%d분"
        private const val NO_REMAIN_BUS_ARRIVAL_TIME = "-분"
        private const val REMAIN_BUS_ARRIVAL_LOCATION = "%d번째 전"
        private const val NO_REMAIN_BUS_ARRIVAL_LOCATION = "-번째 전"
    }
}