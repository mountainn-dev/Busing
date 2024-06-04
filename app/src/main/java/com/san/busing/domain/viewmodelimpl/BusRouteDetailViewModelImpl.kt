package com.san.busing.domain.viewmodelimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.ExceptionMessage
import com.san.busing.data.Success
import com.san.busing.data.repository.BusLocationRepository
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.viewmodel.BusRouteDetailViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class BusRouteDetailViewModelImpl(
    private val busRouteRepository: BusRouteRepository,
    private val busLocationRepository: BusLocationRepository,
    private val routeId: Id,
) : BusRouteDetailViewModel, ViewModel() {
    private val routeInfoLoaded = MutableLiveData<Boolean>()
    private val routeStationLoaded = MutableLiveData<Boolean>()
    private var isLoading = false
    private var error = Const.EMPTY_TEXT

    override val routeInfoReady: LiveData<Boolean>
        get() = routeInfoLoaded
    override val routeStationReady: LiveData<Boolean>
        get() = routeStationLoaded
    override lateinit var routeInfo: BusRouteModel
    override lateinit var routeStation: List<BusStationModel>

    init { load() }

    override fun load() {
        if (!isLoading) {
            isLoading = true

            viewModelScope.launch {
                awaitAll(
                    async { loadRouteInfoContent() },
                    async { loadRouteStationContent() }
                )
                isLoading = false
            }
        }
    }

    private suspend fun loadRouteInfoContent() {
        val result = busRouteRepository.getBusRoute(routeId)

        if (result is Success) {
            routeInfo = result.data()
            routeInfoLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_INFO_EXCEPTION, error)
            routeInfoLoaded.postValue(false)
        }
    }

    private suspend fun loadRouteStationContent() {
        val result = busRouteRepository.getBusStations(routeId)

        if (result is Success) {
            routeStation = result.data()
            routeStationLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_STATIONS_EXCEPTION, error)
            routeStationLoaded.postValue(false)
        }
    }
}