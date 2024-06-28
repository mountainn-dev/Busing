package com.san.busing.domain.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.RouteInfoModel
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.state.UiState

interface RouteDetailViewModel {
    val state: LiveData<UiState>
    val routeInfo: RouteInfoModel
    val routeStations: List<RouteStationModel>
    val routeBuses: List<BusModel>
    val loadableRemainTime: LiveData<Int>
    val bookMark: LiveData<Boolean>
    var error: String

    fun load()
    fun reload()
    fun update(context: Activity)
    fun toggleBookMark()
}