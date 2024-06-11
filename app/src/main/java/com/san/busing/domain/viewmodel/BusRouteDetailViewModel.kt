package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusStationModel

interface BusRouteDetailViewModel {
    val routeInfoReady: LiveData<Boolean>
    val routeStationBusReady: LiveData<Boolean>
    val routeInfo: BusRouteModel
    val routeStation: List<BusStationModel>
    val routeBus: List<BusModel>

    fun load()
}