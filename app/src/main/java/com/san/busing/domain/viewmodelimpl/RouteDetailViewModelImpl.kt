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
import com.san.busing.domain.state.UiState
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
    private var isLoadable = false

    override val state: LiveData<UiState>
        get() = uiState
    private val uiState = MediatorLiveData<UiState>()
    private val routeInfoState = MutableLiveData<UiState>(UiState.Loading)
    private val routeStationState = MutableLiveData<UiState>(UiState.Loading)
    private val routeBusState = MutableLiveData<UiState>(UiState.Loading)
    override lateinit var routeInfo: RouteInfoModel
    override lateinit var routeStations: List<RouteStationModel>
    override lateinit var routeBuses: List<BusModel>

    override val loadableRemainTime: LiveData<Int>
        get() = remainTime
    private val remainTime = MutableLiveData<Int>()
    private val timer = object: CountDownTimer(REMAIN_TOTAL_MILLIS, REMAIN_INTERVAL_MILLIS) {
        override fun onTick(time: Long) {
            if (!isLoadable) isLoadable = true
            remainTime.postValue((time/ REMAIN_INTERVAL_MILLIS).toInt())
        }
        override fun onFinish() {
            isLoadable = false
        }
    }

    override lateinit var error: String

    init {
        merge(uiState, routeInfoState, routeStationState, routeBusState)
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
            routeInfoState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) routeInfoState.postValue(UiState.Timeout)
            if (result.isCritical()) routeInfoState.postValue(UiState.Error)
        }
    }

    private suspend fun loadRouteStationContent() {
        val result = routeRepository.getRouteStations(routeId)

        if (result is Success) {
            routeStations = result.data
            routeStationState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) routeStationState.postValue(UiState.Timeout)
            if (result.isCritical()) routeStationState.postValue(UiState.Error)
        }
    }

    private suspend fun loadRouteBusContent() {
        val result = busLocationRepository.getBusLocations(routeId)

        if (result is Success) {
            routeBuses = result.data.sortedBy { it.sequenceNumber }
            routeBusState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) routeBusState.postValue(UiState.Timeout)
            if (result.isCritical()) routeBusState.postValue(UiState.Error)
        }
    }

    override fun reload() {
        if (!isLoadable) {
            timer.start()
            load()
        }
    }

    /**
     * private fun merge(parent, child1, ...)
     *
     * Ui State 상호작용을 위한 LiveData merge()
     */
    private fun merge(
        parent: MediatorLiveData<UiState>,
        child1: MutableLiveData<UiState>,
        child2: MutableLiveData<UiState>,
        child3: MutableLiveData<UiState>
    ) {
        parent.addSource(child1) { parent.value =  state(it, child2.value!!, child3.value!!) }
        parent.addSource(child2) { parent.value =  state(it, child1.value!!, child3.value!!) }
        parent.addSource(child3) { parent.value =  state(it, child1.value!!, child2.value!!) }
    }

    private fun state(
        state1: UiState, state2: UiState, state3: UiState
    ): UiState {
        return if (isSuccess(state1, state2, state3)) UiState.Success
        else if (isLoading(state1, state2, state3)) UiState.Loading
        else if (isTimeout(state1, state2, state3)) UiState.Timeout
        else UiState.Error
    }

    private fun isSuccess(
        state1: UiState, state2: UiState, state3: UiState
    ) = state1 is UiState.Success && state2 is UiState.Success && state3 is UiState.Success

    private fun isLoading(
        state1: UiState, state2: UiState, state3: UiState
    ) = !isTimeout(state1, state2, state3)
            && (state1 is UiState.Loading || state2 is UiState.Loading || state3 is UiState.Loading)

    private fun isTimeout(
        state1: UiState, state2: UiState, state3: UiState,
    ) = !isCritical(state1, state2, state3)
            && (state1 is UiState.Timeout || state2 is UiState.Timeout || state3 is UiState.Timeout)

    private fun isCritical(
        state1: UiState, state2: UiState, state3: UiState
    ) = state1 is UiState.Error || state2 is UiState.Error || state3 is UiState.Error

    companion object {
        private const val REMAIN_TOTAL_MILLIS: Long = 9999
        private const val REMAIN_INTERVAL_MILLIS: Long = 1000
    }
}