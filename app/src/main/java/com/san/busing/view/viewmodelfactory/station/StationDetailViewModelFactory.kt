package com.san.busing.view.viewmodelfactory.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.route.RouteRepository
import com.san.busing.data.repository.station.StationRepository
import com.san.busing.domain.model.station.StationModel
import com.san.busing.view.viewmodelimpl.station.StationDetailViewModelImpl

class StationDetailViewModelFactory(
    private val stationRepository: StationRepository,
    private val routeRepository: RouteRepository,
    private val station: StationModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StationDetailViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StationDetailViewModelImpl(stationRepository, routeRepository, station) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}