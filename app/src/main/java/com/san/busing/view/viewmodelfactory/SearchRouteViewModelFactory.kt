package com.san.busing.view.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.RouteRepository
import com.san.busing.view.viewmodelimpl.SearchRouteViewModelImpl

class SearchRouteViewModelFactory(
    private val repository: RouteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRouteViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRouteViewModelImpl(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}