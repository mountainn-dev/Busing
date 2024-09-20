package com.san.busing.view.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.RouteRepository
import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.RouteModel
import com.san.busing.view.viewmodelimpl.RouteDetailViewModelImpl

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