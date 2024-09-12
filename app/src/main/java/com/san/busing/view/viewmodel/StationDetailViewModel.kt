package com.san.busing.view.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.BusArrivalModel
import com.san.busing.domain.model.BusArrivalModels
import com.san.busing.domain.model.StationViaRouteModels
import com.san.busing.domain.state.UiState

interface StationDetailViewModel {
    val state: LiveData<UiState>
    val viaRoutes: StationViaRouteModels
    val routeDirection: List<String>
    val busArrivals: List<BusArrivalModel>
    val resetTimer: LiveData<Int>
    val bookMark: LiveData<Boolean>
    var error: String

    fun load()
    fun loadWithTimer()
    fun updateRecentSearch(activity: Activity)
    fun toggleBookMark()
}