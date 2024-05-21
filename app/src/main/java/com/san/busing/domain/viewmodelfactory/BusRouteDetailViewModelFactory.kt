package com.san.busing.domain.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.data.type.Id
import com.san.busing.domain.viewmodelimpl.BusRouteDetailViewModelImpl
import com.san.busing.domain.viewmodelimpl.SearchBusRouteViewModelImpl

class BusRouteDetailViewModelFactory(
    private val repository: BusRouteRepository, private val routeId: Id
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusRouteDetailViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusRouteDetailViewModelImpl(repository, routeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}