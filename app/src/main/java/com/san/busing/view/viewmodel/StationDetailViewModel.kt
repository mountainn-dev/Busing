package com.san.busing.view.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.modelimpl.BusArrivalModel
import com.san.busing.domain.modelimpl.RouteModels
import com.san.busing.domain.modelimpl.RouteStationModel
import com.san.busing.domain.state.UiState

interface StationDetailViewModel {
    val state: LiveData<UiState>
    val viaRoutes: RouteModels
    val nextStations: List<RouteStationModel>
    val busArrivals: List<BusArrivalModel>
    val resetTimer: LiveData<Int>
    val bookMark: LiveData<Boolean>
    var error: String

    fun load()
    fun loadWithTimer()
    fun updateRecentSearch(activity: Activity)
    fun toggleBookMark()
}