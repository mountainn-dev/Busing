package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.RouteInfoModel
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.state.UiState
import java.util.Timer
import java.util.TimerTask

interface RouteDetailViewModel {
    val state: LiveData<UiState>
    val routeInfo: RouteInfoModel
    val routeStations: List<RouteStationModel>
    val routeBuses: List<BusModel>
    val loadableRemainTime: LiveData<Int>
    var error: String

    fun load()
    fun reload()
}