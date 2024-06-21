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
import com.san.busing.domain.utils.Const
import com.san.busing.domain.viewmodel.SearchRouteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchRouteViewModelImpl(
    private val routeRepository: RouteRepository
) : SearchRouteViewModel, ViewModel() {
    private var isSearching = false

    override val searchResultContentReady: LiveData<Boolean>
        get() = searchResultContentLoaded
    private val searchResultContentLoaded = MutableLiveData<Boolean>()
    override val recentSearchContentReady: LiveData<Boolean>
        get() = recentSearchContentLoaded
    private val recentSearchContentLoaded = MutableLiveData<Boolean>()

    override lateinit var routeSummaries: List<RouteSummaryModel>
    override lateinit var routeRecentSearches: List<RouteRecentSearchModel>
    override var keyword = Const.EMPTY_TEXT
    override val serviceErrorState: LiveData<Boolean>
        get() = isSystemError
    private val isSystemError = MutableLiveData<Boolean>()
    override lateinit var error: String

    override fun search(keyword: String) {
        if (!isSearching) {
            isSearching = true
            this.keyword = keyword

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    searchBusRoutes(keyword)
                    isSearching = false
                }
            }
        }
    }

    private suspend fun searchBusRoutes(keyword: String) {
        val result = routeRepository.getRoutes(keyword)

        if (result is Success) {
            // 검색 결과 출력 시 노선 번호, 운행 지역 순으로 출력
            routeSummaries = result.data.sortedWith(compareBy({it.name}, {it.region}))
            searchResultContentLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            searchResultContentLoaded.postValue(false)
            isSystemError.postValue(result.isCritical())
        }
    }

    override fun insert(recentSearchModel: RouteRecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { insertRecentSearch(recentSearchModel) }
        }
    }

    private fun insertRecentSearch(recentSearchModel: RouteRecentSearchModel) {
        val result = routeRepository.insertRecentSearch(recentSearchModel)

        if (result is Error) error = result.message()
    }

    override fun update(recentSearchModel: RouteRecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { updateRecentSearch(recentSearchModel) }
        }
    }

    private fun updateRecentSearch(recentSearchModel: RouteRecentSearchModel) {
        val result = routeRepository.updateRecentSearch(recentSearchModel)

        if (result is Error) error = result.message()
    }

    override fun delete(recentSearchModel: RouteRecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteRecentSearch(recentSearchModel)
                loadRecentSearchContent()
            }
        }
    }

    private fun deleteRecentSearch(recentSearchModel: RouteRecentSearchModel) {
        val result = routeRepository.deleteRecentSearch(recentSearchModel)

        if (result is Error) error = result.message()
    }

    override fun deleteAll(context: Activity) {
        if (isContentReady(recentSearchContentLoaded)) {
            updateRecentSearchIndex(context, Const.ZERO.toLong())   // 최근 검색 인덱스 초기화
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    deleteAllRecentSearch(routeRecentSearches)
                    loadRecentSearchContent()
                }
            }
        }
    }

    private fun deleteAllRecentSearch(recentSearchModels: List<RouteRecentSearchModel>) {
        val result = routeRepository.deleteAllRecentSearch(recentSearchModels)

        if (result is Error) error = result.message()
    }

    override fun restore() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { loadRecentSearchContent() }
        }
    }

    private fun loadRecentSearchContent() {
        val result = routeRepository.getRecentSearch()

        if (result is Success) {
            if (result.data.isEmpty()) recentSearchContentLoaded.postValue(false)
            else {
                routeRecentSearches = result.data.sortedByDescending { it.index }
                recentSearchContentLoaded.postValue(true)
            }
        } else {
            error = (result as Error).message()
            recentSearchContentLoaded.postValue(false)
        }
    }

    override fun clearKeyword() {
        this.keyword = Const.EMPTY_TEXT
    }

    override fun recentSearchIndex(context: Activity): Long {
        val newIndex = getRecentSearchIndex(context)
        updateRecentSearchIndex(context, newIndex)

        return newIndex
    }

    private fun getRecentSearchIndex(context: Activity): Long {
        val result = routeRepository.getRecentSearchIndex(context)

        return (result as Success).data + 1
    }

    private fun updateRecentSearchIndex(context: Activity, newIdx: Long) {
        val result = routeRepository.updateRecentSearchIndex(context, newIdx)

        if (result is Error) error = result.message()
    }

    private fun isContentReady(contentLoaded: MutableLiveData<Boolean>) =
        contentLoaded.isInitialized && contentLoaded.value!!
}