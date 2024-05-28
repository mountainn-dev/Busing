package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import com.san.busing.domain.model.BusRouteInfoModel

interface BusRouteDetailViewModel {
    val routeInfoReady: LiveData<Boolean>
    var routeInfo: BusRouteInfoModel

    fun loadContent()
}