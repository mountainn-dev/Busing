package com.san.busing.domain.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.BusLocationRepository
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.vo.Id
import com.san.busing.domain.viewmodelimpl.BusRouteDetailViewModelImpl

class BusRouteDetailViewModelFactory(
    private val busRouteRepository: BusRouteRepository,
    private val busLocationRepository: BusLocationRepository,
    private val routeId: Id
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusRouteDetailViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusRouteDetailViewModelImpl(busRouteRepository,busLocationRepository, routeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}