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
import com.san.busing.domain.model.BusArrivalModels
import com.san.busing.domain.model.RouteStationModels
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
    private val routeDirectionState = MutableLiveData<UiState>(UiState.Loading)
    private val busArrivalState = MutableLiveData<UiState>(UiState.Loading)
    override lateinit var viaRoutes: StationViaRouteModels
    override lateinit var routeDirection: List<String>
    override lateinit var busArrivals: List<BusArrivalModel>

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

    private var loadingJob: Job? = null

    override lateinit var error: String

    override fun load() {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadViaRoutes()
                if (viaRouteState.value == UiState.Success) {
                    val directions = viaRoutes.get().map { async { getRouteDirection(it.routeSummary.id, it.sequenceNumber) } }
                    val arrivals = viaRoutes.get().map { async { getBusArrival(it.routeSummary.id, it.sequenceNumber) } }
                    directions.map { it.await() }
                    arrivals.map { it.await() }
                }
            }
        }
    }

    private suspend fun loadViaRoutes() {
        val result = stationRepository.getStationViaRoutes(stationId)

        if (result is Success) {
            viaRoutes = result.data
            viaRouteState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) viaRouteState.postValue(UiState.Timeout)
            if (result.isCritical()) viaRouteState.postValue(UiState.Error)
        }
    }

    private suspend fun getRouteDirection(routeId: Id, stationSeq: Int): String? {
        val result = routeRepository.getRouteStations(routeId)

        if (result is Success) {
            val routeStations = result.data
            return if(!isLastStation(routeStations, stationSeq)) routeStations.get(stationSeq-1).name
            else routeStations.get(0).name
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) routeDirectionState.postValue(UiState.Timeout)
            if (result.isCritical()) routeDirectionState.postValue(UiState.Error)
            return null
        }
    }

    private fun isLastStation(stations: RouteStationModels, stationSeq: Int) =
        stations.count() == stationSeq

    private suspend fun getBusArrival(routeId: Id, stationSeq: Int): BusArrivalModel? {
        val result = stationRepository.getBusArrival(stationId, routeId, stationSeq)

        if (result is Success) {
            return result.data
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) busArrivalState.postValue(UiState.Timeout)
            if (result.isCritical()) busArrivalState.postValue(UiState.Error)
            return null
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

    companion object {
        private const val REMAIN_TOTAL_MILLIS: Long = 9999
        private const val TIMER_INTERVAL_MILLIS: Long = 1000
    }
}