package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.RouteInfoModel
import com.san.busing.domain.model.RouteStationModel
import java.util.Timer
import java.util.TimerTask

interface RouteDetailViewModel {
    val routeInfoContentReady: LiveData<Boolean>
    val routeStationContentReady: LiveData<Boolean>
    val loadableRemainTime: LiveData<Int>
    val routeInfo: RouteInfoModel
    val routeStations: List<RouteStationModel>
    val routeBuses: List<BusModel>
    val serviceErrorState: LiveData<Boolean>
    var error: String

    fun load()
    fun reload()
}