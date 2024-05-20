package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData

interface BusRouteDetailViewModel {
    val routeInfoReady: LiveData<Boolean>
}