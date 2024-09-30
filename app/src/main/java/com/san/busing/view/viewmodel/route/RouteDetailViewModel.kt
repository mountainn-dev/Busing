package com.san.busing.view.viewmodel.route

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.modelimpl.route.BusModels
import com.san.busing.domain.modelimpl.route.RouteInfoModel
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.state.UiState

interface RouteDetailViewModel {
    val state: LiveData<UiState>
    val routeInfo: RouteInfoModel
    val viaStations: StationModels
    val buses: BusModels
    val keywordMatchingStationIndex: LiveData<Int>
    val resetTimer: LiveData<Int>
    val bookMark: LiveData<Boolean>
    var error: String

    fun load()
    fun loadWithTimer()
    fun updateRecentSearch(activity: Activity)
    fun toggleBookMark()
    fun find(keyword: String)
    fun moveUpMatchingStation()
    fun moveDownMatchingStation()
}