package com.san.busing.view.viewmodelimpl.route

import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repository.route.RouteRepository
import com.san.busing.domain.model.route.RouteModel
import com.san.busing.domain.modelimpl.route.BusModels
import com.san.busing.domain.modelimpl.route.RouteInfoModel
import com.san.busing.domain.modelimpl.route.RouteRecentSearchModel
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.view.viewmodel.route.RouteDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RouteDetailViewModelImpl(
    private val routeRepository: RouteRepository,
    private val route: RouteModel
) : RouteDetailViewModel, ViewModel() {
    override val state: LiveData<UiState>
        get() = uiState
    private val uiState = MediatorLiveData<UiState>()
    private val routeInfoState = MutableLiveData<UiState>(UiState.Loading)
    private val routeStationState = MutableLiveData<UiState>(UiState.Loading)
    private val routeBusState = MutableLiveData<UiState>(UiState.Loading)
    override lateinit var routeInfo: RouteInfoModel
    override lateinit var viaStations: StationModels
    override lateinit var buses: BusModels
    override val keywordMatchingStationIndex: LiveData<Int>
        get() = matchingStationIndex
    private val matchingStationIndex = MutableLiveData(Const.ZERO)
    private var matchingStationIndexes = listOf<Int>()
    private var indexPointer = Const.ZERO


    override val resetTimer: LiveData<Int>
        get() = remainTime
    private val remainTime = MutableLiveData<Int>()
    private var isLoadable = true
    private val timer = object: CountDownTimer(REMAIN_TOTAL_MILLIS, TIMER_INTERVAL_MILLIS) {
        override fun onTick(time: Long) {
            if (isLoadable) isLoadable = false
            remainTime.postValue((time/ TIMER_INTERVAL_MILLIS).toInt())
        }
        override fun onFinish() {
            isLoadable = true
        }
    }

    override val bookMark: LiveData<Boolean>
        get() = isBookMark
    private val isBookMark = MutableLiveData(false)
    private lateinit var recentSearch: RouteRecentSearchModel

    private var loadingJob: Job? = null

    override lateinit var error: String

    init {
        merge(uiState, routeInfoState, routeStationState, routeBusState)
    }

    override fun load() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                awaitAll(
                    async { loadRouteInfo() },
                    async { loadRouteStations() },
                    async { loadBusLocations() }
                )
            }
        }
    }

    private suspend fun loadRouteInfo() {
        val result = routeRepository.getRouteInfo(route.id)

        if (result is Success) {
            routeInfo = result.data
            routeInfoState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) routeInfoState.postValue(UiState.Timeout)
            if (result.isCritical()) routeInfoState.postValue(UiState.Error)
        }
    }

    private suspend fun loadRouteStations() {
        val result = routeRepository.getRouteStations(route.id)

        if (result is Success) {
            viaStations = result.data
            routeStationState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) routeStationState.postValue(UiState.Timeout)
            if (result.isCritical()) routeStationState.postValue(UiState.Error)
        }
    }

    private suspend fun loadBusLocations() {
        val result = routeRepository.getBusLocations(route.id)

        if (result is Success) {
            buses = result.data
            routeBusState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) routeBusState.postValue(UiState.Timeout)
            if (result.isCritical()) routeBusState.postValue(UiState.Error)
        }
    }

    override fun loadWithTimer() {
        if (isLoadable) {
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
        val result = routeRepository.getRecentSearch(route.id)

        if (result is Success) {
            val model = result.data
            recentSearch = RouteRecentSearchModel(
                model.id, model.type, model.name, model.regionName,
                if (model.bookMark) model.index else nextRecentSearchIndex(activity),
                model.bookMark
            )
        }
        else {
            recentSearch = RouteRecentSearchModel(
                route.id, route.type, route.name, route.regionName,
                nextRecentSearchIndex(activity), false
            )
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
        val result = routeRepository.getRecentSearchIndex(activity)

        return (result as Success).data
    }

    private fun updateRecentSearchIndex(activity: Activity, newIdx: Long) {
        val result = routeRepository.updateRecentSearchIndex(activity, newIdx)

        if (result is Error) error = result.message()
    }

    private suspend fun insertRecentSearch() {
        val result = routeRepository.insertRecentSearch(recentSearch)

        if (result is Error) error = result.message()
    }

    private suspend fun updateRecentSearch() {
        val result = routeRepository.updateRecentSearch(recentSearch)

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
        recentSearch = RouteRecentSearchModel(
            recentSearch.id, recentSearch.type, recentSearch.name, recentSearch.regionName,
            recentSearch.index, !recentSearch.bookMark
        )
    }

    private fun loadBookMarkContent() {
        isBookMark.postValue(recentSearch.bookMark)
    }

    override fun find(keyword: String) {
        if (!::viaStations.isInitialized) return

        matchingStationIndexes = viaStations.findAll(keyword)
        if (indexPointer in matchingStationIndexes.indices)
            matchingStationIndex.postValue(matchingStationIndexes[indexPointer])
    }

    override fun moveUpMatchingStation() {
        if ((indexPointer - 1) !in matchingStationIndexes.indices) return

        matchingStationIndex.postValue(matchingStationIndexes[--indexPointer])
    }

    override fun moveDownMatchingStation() {
        if ((indexPointer + 1) !in matchingStationIndexes.indices) return

        matchingStationIndex.postValue(matchingStationIndexes[++indexPointer])
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