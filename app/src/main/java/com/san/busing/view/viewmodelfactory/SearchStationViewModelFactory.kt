package com.san.busing.view.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.StationRepository
import com.san.busing.view.viewmodelimpl.SearchStationViewModelImpl

class SearchStationViewModelFactory(
    private val repository: StationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchStationViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchStationViewModelImpl(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}