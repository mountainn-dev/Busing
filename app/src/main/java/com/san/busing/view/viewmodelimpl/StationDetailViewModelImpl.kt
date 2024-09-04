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
import com.san.busing.data.repository.BusArrivalRepository
import com.san.busing.data.repository.StationRepository
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusArrivalModels
import com.san.busing.domain.model.StationRecentSearchModel
import com.san.busing.domain.state.UiState
import com.san.busing.view.viewmodel.StationDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StationDetailViewModelImpl(
    private val stationRepository: StationRepository,
    private val stationId: Id,
    private val stationMobileNo: String,
    private val stationName: String,
    private val regionName: String
) : StationDetailViewModel, ViewModel() {
    override val state: LiveData<UiState>
        get() = uiState
    private val uiState = MediatorLiveData<UiState>()
    private val busArrivalState = MutableLiveData<UiState>(UiState.Loading)
    override lateinit var busArrivals: BusArrivalModels

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
                loadBusArrivals()
                // TODO: awaitAll(async { loadBusDirection(busArrival[0]).. })
            }
        }
    }

    private suspend fun loadBusArrivals() {
        val result = stationRepository.getBusArrivals(stationId)

        if (result is Success) {
            busArrivals = result.data
            busArrivalState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) busArrivalState.postValue(UiState.Timeout)
            if (result.isCritical()) busArrivalState.postValue(UiState.Error)
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