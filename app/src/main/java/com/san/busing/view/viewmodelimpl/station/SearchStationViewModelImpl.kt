package com.san.busing.view.viewmodelimpl.station

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repository.station.StationRepository
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.modelimpl.station.StationRecentSearchModel
import com.san.busing.domain.modelimpl.station.StationRecentSearchModels
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.view.viewmodel.station.SearchStationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchStationViewModelImpl(
    private val repository: StationRepository
) : SearchStationViewModel, ViewModel() {
    override val state: LiveData<UiState>
        get() = viewModelState
    private val viewModelState = MutableLiveData<UiState>()
    override lateinit var stations: StationModels

    override val recentSearchContentReady: LiveData<Boolean>
        get() = recentSearchContentLoaded
    private val recentSearchContentLoaded = MutableLiveData<Boolean>()
    override lateinit var stationRecentSearches: StationRecentSearchModels

    override var keyword = Const.EMPTY_TEXT
    private var searchingJob: Job? = null

    override lateinit var error: String

    override fun search(keyword: String) {
        searchingJob?.cancel()
        this.keyword = keyword

        searchingJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchStations()
            }
        }
    }

    private suspend fun searchStations() {
        viewModelState.postValue(UiState.Loading)
        val result = repository.getStations(keyword)

        if (result is Success) {
            stations = result.data
            viewModelState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) viewModelState.postValue(UiState.Timeout)
            if (result.isCritical()) viewModelState.postValue(UiState.Error)
        }
    }

    override fun deleteRecentSearch(itemIdx: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delete(stationRecentSearches.get(itemIdx))
                loadRecentSearchContent()
            }
        }
    }

    private suspend fun delete(recentSearchModel: StationRecentSearchModel) {
        val result = repository.deleteRecentSearch(recentSearchModel)

        if (result is Error) error = result.message()
    }

    private suspend fun loadRecentSearchContent() {
        val result = repository.getAllRecentSearch()

        if (result is Success) {
            if (result.data.isEmpty()) recentSearchContentLoaded.postValue(false)
            else {
                stationRecentSearches = result.data
                recentSearchContentLoaded.postValue(true)
            }
        } else {
            error = (result as Error).message()
            recentSearchContentLoaded.postValue(false)
        }
    }

    override fun deleteAllRecentSearches(activity: Activity) {
        if (dataState(recentSearchContentLoaded)) {
            resetRecentSearchIndex(activity)   // 최근 검색 인덱스 초기화
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    deleteAllRecentSearch()
                    loadRecentSearchContent()
                }
            }
        }
    }

    private fun resetRecentSearchIndex(activity: Activity) {
        val result = repository.updateRecentSearchIndex(activity, DEFAULT_RECENT_SEARCH_INDEX)

        if (result is Error) error = result.message()
    }

    private suspend fun deleteAllRecentSearch() {
        val result = repository.deleteAllRecentSearch()

        if (result is Error) error = result.message()
    }

    override fun clearKeyword() {
        this.keyword = Const.EMPTY_TEXT
    }

    override fun restore() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { loadRecentSearchContent() }
        }
    }

    private fun dataState(data: MutableLiveData<Boolean>) = data.isInitialized && data.value!!

    companion object {
        private const val DEFAULT_RECENT_SEARCH_INDEX: Long = 0
    }
}