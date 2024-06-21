package com.san.busing.domain.viewmodelimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.repository.BusLocationRepository
import com.san.busing.data.repository.RouteRepository
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.RouteInfoModel
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.viewmodel.RouteDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RouteDetailViewModelImpl(
    private val routeRepository: RouteRepository,
    private val busLocationRepository: BusLocationRepository,
    private val routeId: Id,
) : RouteDetailViewModel, ViewModel() {
    private var isLoading = false

    override val routeInfoContentReady: LiveData<Boolean>
        get() = routeInfoLoaded
    private val routeInfoLoaded = MutableLiveData<Boolean>()

    override val routeStationContentReady: LiveData<Boolean>
        get() = routeStationAndBusLoaded
    private val routeStationAndBusLoaded = MediatorLiveData<Boolean>()
    private val routeStationLoaded = MutableLiveData<Boolean>()
    private val routeBusLoaded = MutableLiveData<Boolean>()

    override lateinit var routeInfo: RouteInfoModel
    override lateinit var routeStations: List<RouteStationModel>
    override lateinit var routeBuses: List<BusModel>
    override val serviceErrorState: LiveData<Boolean>
        get() = isSystemError
    private val isSystemError = MutableLiveData<Boolean>()
    override lateinit var error: String

    init {
        load(routeId)
        merge(routeStationAndBusLoaded, routeStationLoaded, routeBusLoaded)
    }

    override fun load(routeId: Id) {
        if (!isLoading) {
            isLoading = true

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    awaitAll(
                        async { loadRouteInfoContent(routeId) },
                        async { loadRouteStationContent(routeId) },
                        async { loadRouteBusContent(routeId) }
                    )
                }
                isLoading = false
            }
        }
    }

    private suspend fun loadRouteInfoContent(routeId: Id) {
        val result = routeRepository.getRouteInfo(routeId)

        if (result is Success) {
            routeInfo = result.data
            routeInfoLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_INFO_EXCEPTION, error)
            routeInfoLoaded.postValue(false)
            isSystemError.postValue(result.isCritical())
        }
    }

    private suspend fun loadRouteStationContent(routeId: Id) {
        val result = routeRepository.getRouteStations(routeId)

        if (result is Success) {
            routeStations = result.data
            routeStationLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_STATIONS_EXCEPTION, error)
            routeStationLoaded.postValue(false)
            isSystemError.postValue(result.isCritical())
        }
    }

    private suspend fun loadRouteBusContent(routeId: Id) {
        val result = busLocationRepository.getBusLocations(routeId)

        if (result is Success) {
            routeBuses = result.data.sortedBy { it.sequenceNumber }
            routeBusLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_BUS_EXCEPTION, error)
            routeBusLoaded.postValue(false)
            isSystemError.postValue(result.isCritical())
        }
    }

    /**
     * private fun merge(parent, child1, child2)
     *
     * Data Load State 를 확인하기 위한 LiveData merge()
     */
    private fun merge(
        parent: MediatorLiveData<Boolean>,
        child1: MutableLiveData<Boolean>, child2: MutableLiveData<Boolean>
    ) {
        parent.addSource(child1) { parent.value = it && dataState(child2) }
        parent.addSource(child2) { parent.value = it && dataState(child1) }
    }

    private fun dataState(data: MutableLiveData<Boolean>) = data.isInitialized && data.value!!
}