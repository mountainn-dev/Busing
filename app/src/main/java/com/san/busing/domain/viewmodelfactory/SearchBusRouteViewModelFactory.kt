package com.san.busing.domain.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.BusRepository
import com.san.busing.domain.viewmodelimpl.SearchBusRouteViewModelImpl

class SearchBusRouteViewModelFactory(
    private val repository: BusRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBusRouteViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchBusRouteViewModelImpl(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}