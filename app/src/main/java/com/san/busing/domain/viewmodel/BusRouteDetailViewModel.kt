package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import com.san.busing.domain.model.BusRouteModel

interface BusRouteDetailViewModel {
    val routeInfoReady: LiveData<Boolean>
    var routeInfo: BusRouteModel

    fun loadContent()
}