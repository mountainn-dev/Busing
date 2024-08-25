package com.san.busing.view.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.StationRepository
import com.san.busing.data.vo.Id
import com.san.busing.view.viewmodelimpl.StationDetailViewModelImpl

class StationDetailViewModelFactory(
    private val repository: StationRepository,
    private val stationId: Id,
    private val stationMobileNo: String,
    private val stationName: String,
    private val regionName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StationDetailViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StationDetailViewModelImpl(repository, stationId, stationMobileNo, stationName, regionName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}