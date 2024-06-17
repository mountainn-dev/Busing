package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusStationModel

interface BusRouteDetailViewModel {
    val routeInfoContentReady: LiveData<Boolean>
    val routeStationBusContentReady: LiveData<Boolean>
    val routeInfoContent: BusRouteModel
    val routeStationContent: List<BusStationModel>
    val routeBusContent: List<BusModel>

    fun load(routeId: Id)
}