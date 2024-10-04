package com.san.busing.view.viewmodelfactory.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.route.RouteRepository
import com.san.busing.view.viewmodelimpl.route.SearchRouteViewModelImpl

class SearchRouteViewModelFactory(
    private val repository: RouteRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRouteViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRouteViewModelImpl(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
