package com.san.busing.view.viewmodelimpl

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repository.RouteRepository
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteRecentSearchModels
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.model.RouteSummaryModels
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.view.viewmodel.SearchRouteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchRouteViewModelImpl(
    private val repository: RouteRepository
) : SearchRouteViewModel, ViewModel() {
    override val state: LiveData<UiState>
        get() = viewModelState
    private val viewModelState = MutableLiveData<UiState>()
    override lateinit var routeSummaries: RouteSummaryModels

    override val recentSearchContentReady: LiveData<Boolean>
        get() = recentSearchContentLoaded
    private val recentSearchContentLoaded = MutableLiveData<Boolean>()
    override lateinit var routeRecentSearches: RouteRecentSearchModels

    override var keyword = Const.EMPTY_TEXT
    private var searchingJob: Job? = null

    override lateinit var error: String

    override fun search(keyword: String) {
        searchingJob?.cancel()
        this.keyword = keyword

        searchingJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchRoutes()
            }
        }
    }

    private suspend fun searchRoutes() {
        viewModelState.postValue(UiState.Loading)
        val result = repository.getRoutes(keyword)

        if (result is Success) {
            routeSummaries = result.data
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
                delete(routeRecentSearches.get(itemIdx))
                loadRecentSearchContent()
            }
        }
    }

    private suspend fun delete(recentSearchModel: RouteRecentSearchModel) {
        val result = repository.deleteRecentSearch(recentSearchModel)

        if (result is Error) error = result.message()
    }

    override fun deleteAllRecentSearches(activity: Activity) {
        if (dataState(recentSearchContentLoaded)) {
            resetRecentSearchIndex(activity)
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

    private suspend fun loadRecentSearchContent() {
        val result = repository.getAllRecentSearch()

        if (result is Success) {
            if (result.data.isEmpty()) recentSearchContentLoaded.postValue(false)
            else {
                routeRecentSearches = result.data
                recentSearchContentLoaded.postValue(true)
            }
        } else {
            error = (result as Error).message()
            recentSearchContentLoaded.postValue(false)
        }
    }

    private fun dataState(data: MutableLiveData<Boolean>) = data.isInitialized && data.value!!

    companion object {
        private const val DEFAULT_RECENT_SEARCH_INDEX: Long = 0
    }
}