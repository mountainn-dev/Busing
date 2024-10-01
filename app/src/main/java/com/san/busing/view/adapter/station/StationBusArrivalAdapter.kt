package com.san.busing.view.adapter.station

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.databinding.ItemStationBusArrivalBinding
import com.san.busing.domain.model.Passable
import com.san.busing.domain.model.Stoppable
import com.san.busing.domain.model.station.StationModel
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.modelimpl.station.BusArrivalModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.listener.ItemClickEventListener

class StationBusArrivalAdapter(
    private val routeItems: RouteModels,
    private val nextStations: List<StationModel>,
    private val busArrivals: List<BusArrivalModel>,
    private val itemClickEventListener: ItemClickEventListener,
    private val activity: Activity
) : RecyclerView.Adapter<StationBusArrivalAdapter.StationBusArrivalViewHolder>(){
    inner class StationBusArrivalViewHolder(
        private val binding: ItemStationBusArrivalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            loadContent(position)
            setContentColor(position)
            setItemClickEventListener(position)
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
            val nextStationName = nextStations.find {
                routeItems.get(position).isSame((it as Passable).vehicleId)
                        && (routeItems.get(position) as Stoppable).stationSequence == it.stationSequence
            }?.name

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
            val busArrival = busArrivals.find {
                routeItems.get(position).isSame(it.id)
                        && (routeItems.get(position) as Stoppable).stationSequence == it.sequenceNumber
            }

            if (busArrival != null) {
                if (!busArrival.arrivalFlag.isStop()) loadBusArrival(busArrival)
                else loadBusArrivalFlag(busArrival)
            } else unloadBusArrival()
        }

        private fun loadBusArrival(busArrival: BusArrivalModel) {
            if (busArrival.predictTimeFirst == Const.ZERO) toggleBusArrivalFirst(binding.txtNoArrivalFirst)
            else loadBusArrivalFirst(busArrival)
            if (busArrival.predictTimeSecond == Const.ZERO) toggleBusArrivalSecond(binding.txtNoArrivalSecond)
            else loadBusArrivalSecond(busArrival)
        }

        private fun loadBusArrivalFirst(busArrival: BusArrivalModel) {
            binding.txtPredictTimeFirst.text = busArrivalPredictTimeMessage(busArrival.predictTimeFirst)
            binding.txtLocationFirst.text = busArrivalLocationMessage(busArrival.locationFirst)

            toggleBusArrivalFirst(binding.llArrivalFirst)
        }

        private fun loadBusArrivalSecond(busArrival: BusArrivalModel) {
            binding.txtPredictTimeSecond.text = busArrivalPredictTimeMessage(busArrival.predictTimeSecond)
            binding.txtLocationSecond.text = busArrivalLocationMessage(busArrival.locationSecond)

            toggleBusArrivalSecond(binding.llArrivalSecond)
        }

        private fun loadBusArrivalFlag(busArrival: BusArrivalModel) {
            binding.txtBusArrivalFlagFirst.text = busArrival.arrivalFlag.flagName
            binding.txtBusArrivalFlagSecond.text = busArrival.arrivalFlag.flagName

            toggleBusArrivalFirst(binding.txtBusArrivalFlagFirst)
            toggleBusArrivalSecond(binding.txtBusArrivalFlagSecond)
        }

        private fun unloadBusArrival() {
            toggleBusArrivalFirst(binding.txtNoArrivalFirst)
            toggleBusArrivalSecond(binding.txtNoArrivalSecond)
        }

        private fun toggleBusArrivalFirst(view: View) {
            binding.llArrivalFirst.visibility = visibleWhenTrue(view == binding.llArrivalFirst)
            binding.txtBusArrivalFlagFirst.visibility = visibleWhenTrue(view == binding.txtBusArrivalFlagFirst)
            binding.txtNoArrivalFirst.visibility = visibleWhenTrue(view == binding.txtNoArrivalFirst)
        }

        private fun toggleBusArrivalSecond(view: View) {
            binding.llArrivalSecond.visibility = visibleWhenTrue(view == binding.llArrivalSecond)
            binding.txtBusArrivalFlagSecond.visibility = visibleWhenTrue(view == binding.txtBusArrivalFlagSecond)
            binding.txtNoArrivalSecond.visibility = visibleWhenTrue(view == binding.txtNoArrivalSecond)
        }

        private fun setContentColor(position: Int) {
            val color = ContextCompat.getColor(
                activity,
                Utils.getColorByRouteType(routeItems.get(position).type)
            )

            binding.txtRouteName.setTextColor(color)
        }

        private fun setItemClickEventListener(position: Int) {
            binding.clStationBusArrivalItem.setOnClickListener {
                itemClickEventListener.onItemClickListener(position)
            }
        }

        private fun visibleWhenTrue(state: Boolean) = if (state) View.VISIBLE else View.GONE
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
        binding.txtRouteName.setHorizontallyScrolling(true)
        binding.txtRouteName.isSelected = true
        binding.txtNextStationName.setHorizontallyScrolling(true)
        binding.txtNextStationName.isSelected = true
    }

    override fun getItemCount() = routeItems.count()

    override fun onBindViewHolder(holder: StationBusArrivalViewHolder, position: Int) {
        holder.bind(position)
    }

    companion object {
        private const val REMAIN_BUS_ARRIVAL_TIME = "%d분"
        private const val REMAIN_BUS_ARRIVAL_LOCATION = "[%d번째 전]"
    }
}