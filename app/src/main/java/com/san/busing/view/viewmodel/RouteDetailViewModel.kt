package com.san.busing.view.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.modelimpl.BusModels
import com.san.busing.domain.modelimpl.RouteInfoModel
import com.san.busing.domain.modelimpl.StationModels
import com.san.busing.domain.state.UiState

interface RouteDetailViewModel {
    val state: LiveData<UiState>
    val routeInfo: RouteInfoModel
    val routeStations: StationModels
    val routeBuses: BusModels
    val resetTimer: LiveData<Int>
    val bookMark: LiveData<Boolean>
    var error: String

    fun load()
    fun loadWithTimer()
    fun updateRecentSearch(activity: Activity)
    fun toggleBookMark()
}