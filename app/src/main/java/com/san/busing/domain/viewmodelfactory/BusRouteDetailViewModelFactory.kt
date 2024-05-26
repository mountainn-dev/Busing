package com.san.busing.domain.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.viewmodelimpl.BusRouteDetailViewModelImpl

class BusRouteDetailViewModelFactory(
    private val repository: BusRouteRepository, private val recentSearchModel: BusRouteRecentSearchModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusRouteDetailViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusRouteDetailViewModelImpl(repository, recentSearchModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}