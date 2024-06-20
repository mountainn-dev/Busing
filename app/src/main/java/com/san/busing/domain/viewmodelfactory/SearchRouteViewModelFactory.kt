package com.san.busing.domain.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.RouteRepository
import com.san.busing.domain.viewmodelimpl.SearchRouteViewModelImpl

class SearchRouteViewModelFactory(
    private val routeRepository: RouteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRouteViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRouteViewModelImpl(routeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}