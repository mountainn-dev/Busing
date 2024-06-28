package com.san.busing.domain.viewmodelimpl

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repository.RouteRepository
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.domain.viewmodel.SearchRouteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchRouteViewModelImpl(
    private val routeRepository: RouteRepository
) : SearchRouteViewModel, ViewModel() {
    override val state: LiveData<UiState>
        get() = searchResultState
    private val searchResultState = MutableLiveData<UiState>()
    override lateinit var routeSummaries: List<RouteSummaryModel>

    override val recentSearchContentReady: LiveData<Boolean>
        get() = recentSearchContentLoaded
    private val recentSearchContentLoaded = MutableLiveData<Boolean>()
    override lateinit var routeRecentSearches: List<RouteRecentSearchModel>

    override var keyword = Const.EMPTY_TEXT
    override lateinit var error: String
    private var isSearching = false

    override fun search(keyword: String) {
        if (!isSearching) {
            isSearching = true
            this.keyword = keyword

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    searchBusRoutes()
                    isSearching = false
                }
            }
        }
    }

    private suspend fun searchBusRoutes() {
        searchResultState.postValue(UiState.Loading)
        val result = routeRepository.getRoutes(keyword)

        if (result is Success) {
            // 검색 결과 출력 시 노선 번호, 운행 지역 순으로 출력
            routeSummaries = result.data.sortedWith(compareBy({it.name}, {it.region}))
            searchResultState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) searchResultState.postValue(UiState.Timeout)
            if (result.isCritical()) searchResultState.postValue(UiState.Error)
        }
    }

    override fun delete(recentSearchModel: RouteRecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteRecentSearch(recentSearchModel)
                loadRecentSearchContent()
            }
        }
    }

    private suspend fun deleteRecentSearch(recentSearchModel: RouteRecentSearchModel) {
        val result = routeRepository.deleteRecentSearch(recentSearchModel)

        if (result is Error) error = result.message()
    }

    override fun deleteAll(context: Activity) {
        if (dataState(recentSearchContentLoaded)) {
            resetRecentSearchIndex(context)   // 최근 검색 인덱스 초기화
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    deleteAllRecentSearch()
                    loadRecentSearchContent()
                }
            }
        }
    }

    private fun resetRecentSearchIndex(context: Activity) {
        val result = routeRepository.updateRecentSearchIndex(context, DEFAULT_RECENT_SEARCH_INDEX)

        if (result is Error) error = result.message()
    }

    private suspend fun deleteAllRecentSearch() {
        val result = routeRepository.deleteAllRecentSearch(routeRecentSearches)

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
        val result = routeRepository.getRecentSearches()

        if (result is Success) {
            if (result.data.isEmpty()) recentSearchContentLoaded.postValue(false)
            else {
                routeRecentSearches = result.data.sortedWith(
                    compareByDescending<RouteRecentSearchModel> { it.bookMark }.thenByDescending { it.index })
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