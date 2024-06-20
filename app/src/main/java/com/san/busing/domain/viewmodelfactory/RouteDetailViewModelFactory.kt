package com.san.busing.domain.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.BusLocationRepository
import com.san.busing.data.repository.RouteRepository
import com.san.busing.data.vo.Id
import com.san.busing.domain.viewmodelimpl.RouteDetailViewModelImpl

class RouteDetailViewModelFactory(
    private val routeRepository: RouteRepository,
    private val busLocationRepository: BusLocationRepository,
    private val routeId: Id
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RouteDetailViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RouteDetailViewModelImpl(routeRepository,busLocationRepository, routeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}