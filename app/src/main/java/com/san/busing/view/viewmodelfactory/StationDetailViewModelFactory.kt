package com.san.busing.view.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.san.busing.data.repository.BusArrivalRepository
import com.san.busing.data.repository.StationRepository
import com.san.busing.data.vo.Id
import com.san.busing.view.viewmodelimpl.StationDetailViewModelImpl

class StationDetailViewModelFactory(
    private val stationRepository: StationRepository,
    private val busArrivalRepository: BusArrivalRepository,
    private val stationId: Id,
    private val stationMobileNo: String,
    private val stationName: String,
    private val regionName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StationDetailViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StationDetailViewModelImpl(
                stationRepository, busArrivalRepository,
                stationId, stationMobileNo, stationName, regionName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}