package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusStationModel

interface BusRouteDetailViewModel {
    val routeInfoReady: LiveData<Boolean>
    val routeStationReady: LiveData<Boolean>
    val routeInfo: BusRouteModel
    val routeStation: List<BusStationModel>

    fun load()
}