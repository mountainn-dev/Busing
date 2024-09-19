package com.san.busing.view.viewmodelimpl

import android.app.Activity
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repository.RouteRepository
import com.san.busing.data.repository.StationRepository
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusArrivalModel
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.model.StationRecentSearchModel
import com.san.busing.domain.model.StationViaRouteModels
import com.san.busing.domain.state.UiState
import com.san.busing.view.viewmodel.StationDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Stack

class StationDetailViewModelImpl(
    private val stationRepository: StationRepository,
    private val routeRepository: RouteRepository,
    private val stationId: Id,
    private val stationMobileNo: String,
    private val stationName: String,
    private val regionName: String
) : StationDetailViewModel, ViewModel() {
    override val state: LiveData<UiState>
        get() = uiState
    private val uiState = MediatorLiveData<UiState>()
    private val viaRouteState = MutableLiveData<UiState>(UiState.Loading)
    private val nextStationState = MutableLiveData<UiState>(UiState.Loading)
    private val busArrivalState = MutableLiveData<UiState>(UiState.Loading)
    override lateinit var viaRoutes: StationViaRouteModels
    override val nextStations = Stack<RouteStationModel>()
    override val busArrivals = Stack<BusArrivalModel>()

    override val resetTimer: LiveData<Int>
        get() = remainTime
    private val remainTime = MutableLiveData<Int>()
    private var isLoadable = false
    private val timer = object: CountDownTimer(REMAIN_TOTAL_MILLIS, TIMER_INTERVAL_MILLIS) {
        override fun onTick(time: Long) {
            if (!isLoadable) isLoadable = true
            remainTime.postValue((time/ TIMER_INTERVAL_MILLIS).toInt())
        }
        override fun onFinish() {
            isLoadable = false
        }
    }

    override val bookMark: LiveData<Boolean>
        get() = isBookMark
    private val isBookMark = MutableLiveData(false)
    private lateinit var recentSearch: StationRecentSearchModel

    private var viaRouteLoadingJob: Job? = null
    private var busLoadingJob: Job? = null

    override lateinit var error: String

    init {
        merge(uiState, viaRouteState, nextStationState, busArrivalState)
    }

    override fun load() {
        viaRouteLoadingJob?.cancel()

        viaRouteLoadingJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadViaRoutes()
            }
        }
    }

    private suspend fun loadViaRoutes() {
        val result = stationRepository.getStationViaRoutes(stationId)

        if (result is Success) {
            viaRoutes = result.data
            viaRouteState.postValue(UiState.Success)
            loadRouteDirectionAndBusArrival()
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) viaRouteState.postValue(UiState.Timeout)
            if (result.isCritical()) viaRouteState.postValue(UiState.Error)
        }
    }

    private fun loadRouteDirectionAndBusArrival() {
        busLoadingJob?.cancel()
        nextStations.clear()
        busArrivals.clear()

        busLoadingJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                viaRoutes.get().flatMap {
                    listOf(
                        async { loadNextStation(it.routeSummary.id, it.sequenceNumber) },
                        async { loadBusArrival(it.routeSummary.id, it.sequenceNumber) }
                    )
                }.awaitAll().let {
                    nextStationState.postValue(UiState.Success)
                    busArrivalState.postValue(UiState.Success)
                }
            }
        }
    }

    private suspend fun loadNextStation(routeId: Id, stationSeq: Int) {
        val result = routeRepository.getRouteStations(routeId)

        if (result is Success) {
            val nextStation = result.data.getOrFirst(stationSeq)
            nextStation.setViaRouteId(routeId)
            nextStations.push(nextStation)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) nextStationState.postValue(UiState.Timeout)
            if (result.isCritical()) nextStationState.postValue(UiState.Error)
        }
    }

    private suspend fun loadBusArrival(routeId: Id, stationSeq: Int) {
        val result = stationRepository.getBusArrival(stationId, routeId, stationSeq)

        if (result is Success) {
            busArrivals.push(result.data)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) busArrivalState.postValue(UiState.Timeout)
        }
    }

    override fun loadWithTimer() {
        if (!isLoadable) {
            timer.start()
            load()
        }
    }

    override fun updateRecentSearch(
        activity: Activity
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadRecentSearch(activity)
                loadBookMarkContent()
                insertRecentSearch()
                updateRecentSearch()
            }
        }
    }

    private suspend fun loadRecentSearch(activity: Activity) {
        val result = stationRepository.getRecentSearch(stationId)

        if (result is Success) {
            val model = result.data
            recentSearch = StationRecentSearchModel(
                model.id, model.mobileNo, model.name, model.regionName,
                if (model.bookMark) model.index else nextRecentSearchIndex(activity),
                model.bookMark)
        }
        else {
            recentSearch = StationRecentSearchModel(
                stationId, stationMobileNo, stationName, regionName,
                nextRecentSearchIndex(activity), false)
            isBookMark.postValue(false)
            error = (result as Error).message()
        }
    }

    private fun nextRecentSearchIndex(activity: Activity): Long {
        val newIndex = previousRecentSearchIndex(activity) + 1
        updateRecentSearchIndex(activity, newIndex)

        return newIndex
    }

    private fun previousRecentSearchIndex(activity: Activity): Long {
        val result = stationRepository.getRecentSearchIndex(activity)

        return (result as Success).data
    }

    private fun updateRecentSearchIndex(activity: Activity, newIdx: Long) {
        val result = stationRepository.updateRecentSearchIndex(activity, newIdx)

        if (result is Error) error = result.message()
    }

    private suspend fun insertRecentSearch() {
        val result = stationRepository.insertRecentSearch(recentSearch)

        if (result is Error) error = result.message()
    }

    private suspend fun updateRecentSearch() {
        val result = stationRepository.updateRecentSearch(recentSearch)

        if (result is Error) error = result.message()
    }

    override fun toggleBookMark() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                changeBookMarkStatus()
                updateRecentSearch()
                loadBookMarkContent()
            }
        }
    }

    private fun changeBookMarkStatus() {
        recentSearch = StationRecentSearchModel(
            recentSearch.id, recentSearch.mobileNo, recentSearch.name, recentSearch.regionName,
            recentSearch.index, !recentSearch.bookMark
        )
    }

    private fun loadBookMarkContent() {
        isBookMark.postValue(recentSearch.bookMark)
    }

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
        state1: UiState, state2: UiState, state3: UiState,
    ) = if (isCritical(state1, state2, state3)) UiState.Error
    else if (isTimeout(state1, state2, state3)) UiState.Timeout
    else if (isLoading(state1, state2, state3)) UiState.Loading
    else UiState.Success

    private fun isLoading(
        state1: UiState, state2: UiState, state3: UiState
    ) = state1 is UiState.Loading || state2 is UiState.Loading || state3 is UiState.Loading

    private fun isTimeout(
        state1: UiState, state2: UiState, state3: UiState,
    ) = state1 is UiState.Timeout || state2 is UiState.Timeout || state3 is UiState.Timeout

    private fun isCritical(
        state1: UiState, state2: UiState, state3: UiState
    ) = state1 is UiState.Error || state2 is UiState.Error || state3 is UiState.Error

    companion object {
        private const val REMAIN_TOTAL_MILLIS: Long = 9999
        private const val TIMER_INTERVAL_MILLIS: Long = 1000
    }
}