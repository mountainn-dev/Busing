package com.san.busing.view.viewmodelfactory.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.route.RouteRepository
import com.san.busing.domain.model.route.RouteModel
import com.san.busing.view.viewmodelimpl.route.RouteDetailViewModelImpl

class RouteDetailViewModelFactory(
    private val routeRepository: RouteRepository,
    private val route: RouteModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RouteDetailViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RouteDetailViewModelImpl(routeRepository, route) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}