package com.san.busing.domain.viewmodelimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.Success
import com.san.busing.data.repository.BusLocationRepository
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModel
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
    private var isLoading = false
    private var error = Const.EMPTY_TEXT

    override val routeInfoContentReady: LiveData<Boolean>
        get() = routeInfoLoaded
    private val routeInfoLoaded = MutableLiveData<Boolean>()

    override val routeStationBusContentReady: LiveData<Boolean>
        get() = routeStationBusLoaded
    private val routeStationBusLoaded = MediatorLiveData<Boolean>()
    private val routeStationLoaded = MutableLiveData<Boolean>()
    private val routeBusLoaded = MutableLiveData<Boolean>()

    override lateinit var routeInfoContent: BusRouteModel
    override lateinit var routeStationContent: List<BusStationModel>
    override lateinit var routeBusContent: List<BusModel>

    init {
        load(routeId)
        merge(routeStationBusLoaded, routeStationLoaded, routeBusLoaded)
    }

    override fun load(routeId: Id) {
        if (!isLoading) {
            isLoading = true

            viewModelScope.launch {
                awaitAll(
                    async { loadRouteInfoContent(routeId) },
                    async { loadRouteStationContent(routeId) },
                    async { loadRouteBusContent(routeId) }
                )
                isLoading = false
            }
        }
    }

    private suspend fun loadRouteInfoContent(routeId: Id) {
        val result = busRouteRepository.getBusRoute(routeId)

        if (result is Success) {
            routeInfoContent = result.data()
            routeInfoLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_INFO_EXCEPTION, error)
            routeInfoLoaded.postValue(false)
        }
    }

    private suspend fun loadRouteStationContent(routeId: Id) {
        val result = busRouteRepository.getBusStations(routeId)

        if (result is Success) {
            routeStationContent = result.data()
            routeStationLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_STATIONS_EXCEPTION, error)
            routeStationLoaded.postValue(false)
        }
    }

    private suspend fun loadRouteBusContent(routeId: Id) {
        val result = busLocationRepository.getBusLocations(routeId)

        if (result is Success) {
            routeBusContent = result.data().sortedBy { it.sequenceNumber }
            routeBusLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_BUS_EXCEPTION, error)
            routeBusLoaded.postValue(false)
        }
    }

    private fun merge(
        parent: MediatorLiveData<Boolean>,
        child1: MutableLiveData<Boolean>, child2: MutableLiveData<Boolean>
    ) {
        parent.addSource(child1) { parent.value = it && isContentReady(child2) }
        parent.addSource(child2) { parent.value = it && isContentReady(child1) }
    }

    private fun isContentReady(contentLoaded: MutableLiveData<Boolean>) =
        contentLoaded.isInitialized && contentLoaded.value!!
}