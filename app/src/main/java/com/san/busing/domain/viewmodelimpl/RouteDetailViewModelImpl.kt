package com.san.busing.domain.viewmodelimpl

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
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

    override val loadableRemainTime: LiveData<Int>
        get() = remainTime
    private val remainTime = MutableLiveData<Int>()
    private val timer = object: CountDownTimer(TOTAL_MILLIS, INTERVAL_MILLIS) {
        override fun onTick(time: Long) {
            if (!isLoading) isLoading = true
            remainTime.postValue((time/ INTERVAL_MILLIS).toInt())
        }
        override fun onFinish() {
            isLoading = false
        }
    }

    override lateinit var routeInfo: RouteInfoModel
    override lateinit var routeStations: List<RouteStationModel>
    override lateinit var routeBuses: List<BusModel>
    override val serviceErrorState: LiveData<Boolean>
        get() = isCriticalError
    private val isCriticalError = MutableLiveData<Boolean>()
    override lateinit var error: String

    init {
        merge(routeStationAndBusLoaded, routeStationLoaded, routeBusLoaded)
    }

    override fun load() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                awaitAll(
                    async { loadRouteInfoContent() },
                    async { loadRouteStationContent() },
                    async { loadRouteBusContent() }
                )
            }
        }
    }

    private suspend fun loadRouteInfoContent() {
        val result = routeRepository.getRouteInfo(routeId)

        if (result is Success) {
            routeInfo = result.data
            routeInfoLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            routeInfoLoaded.postValue(false)
            isCriticalError.postValue(result.isCritical())
        }
    }

    private suspend fun loadRouteStationContent() {
        val result = routeRepository.getRouteStations(routeId)

        if (result is Success) {
            routeStations = result.data
            routeStationLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            routeStationLoaded.postValue(false)
            isCriticalError.postValue(result.isCritical())
        }
    }

    private suspend fun loadRouteBusContent() {
        val result = busLocationRepository.getBusLocations(routeId)

        if (result is Success) {
            routeBuses = result.data.sortedBy { it.sequenceNumber }
            routeBusLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            routeBusLoaded.postValue(false)
            isCriticalError.postValue(result.isCritical())
        }
    }

    override fun reload() {
        if (!isLoading) {
            timer.start()
            load()
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
    }

    private fun dataState(data: MutableLiveData<Boolean>) = data.isInitialized && data.value!!

    companion object {
        private const val TOTAL_MILLIS: Long = 10000
        private const val INTERVAL_MILLIS: Long = 1000
    }
}