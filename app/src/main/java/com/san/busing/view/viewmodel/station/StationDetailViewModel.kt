package com.san.busing.view.viewmodel.station

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.station.StationModel
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.modelimpl.station.BusArrivalModel
import com.san.busing.domain.state.UiState

interface StationDetailViewModel {
    val state: LiveData<UiState>
    val viaRoutes: RouteModels
    val nextStations: List<StationModel>
    val busArrivals: List<BusArrivalModel>
    val resetTimer: LiveData<Int>
    val bookMark: LiveData<Boolean>
    var error: String

    fun load()
    fun loadWithTimer()
    fun updateRecentSearch(activity: Activity)
    fun toggleBookMark()
}