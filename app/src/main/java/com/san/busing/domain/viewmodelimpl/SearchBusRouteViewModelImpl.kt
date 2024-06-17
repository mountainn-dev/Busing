package com.san.busing.domain.viewmodelimpl

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.Success
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.model.BusRouteSearchResultModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.viewmodel.SearchBusRouteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchBusRouteViewModelImpl(
    private val repository: BusRouteRepository
) : SearchBusRouteViewModel, ViewModel() {
    private val searchResultContentLoaded = MutableLiveData<Boolean>()
    private val recentSearchContentLoaded = MutableLiveData<Boolean>()
    private var isSearching = false
    private var error = Const.EMPTY_TEXT

    override val searchResultContentReady: LiveData<Boolean>
        get() = searchResultContentLoaded
    override val recentSearchContentReady: LiveData<Boolean>
        get() = recentSearchContentLoaded
    override lateinit var searchResultContent: List<BusRouteSearchResultModel>
    override lateinit var recentSearchContent: List<BusRouteRecentSearchModel>
    override var keyword = Const.EMPTY_TEXT

    override fun search(keyword: String) {
        if (!isSearching) {
            isSearching = true
            this.keyword = keyword

            viewModelScope.launch {
                searchBusRoutes(keyword)
                isSearching = false
            }
        }
    }

    private suspend fun searchBusRoutes(keyword: String) {
        val result = repository.getBusRoutes(keyword)

        if (result is Success) {
            // 검색 결과 출력 시 노선 번호, 운행 지역 순으로 출력
            searchResultContent = result.data().sortedWith(compareBy({it.name}, {it.region}))
            searchResultContentLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_BUS_ROUTE_EXCEPTION, error)
            searchResultContentLoaded.postValue(false)
        }
    }

    override fun insert(recentSearchModel: BusRouteRecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { insertRecentSearch(recentSearchModel) }
        }
    }

    private fun insertRecentSearch(recentSearchModel: BusRouteRecentSearchModel) {
        val result = repository.insertRecentSearch(recentSearchModel)

        if (result is Error) {
            error = result.message()
            Log.e(ExceptionMessage.TAG_RECENT_SEARCH_EXCEPTION, error)
        }
    }

    override fun update(recentSearchModel: BusRouteRecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { updateRecentSearch(recentSearchModel) }
        }
    }

    private fun updateRecentSearch(recentSearchModel: BusRouteRecentSearchModel) {
        val result = repository.updateRecentSearch(recentSearchModel)

        if (result is Error) {
            error = result.message()
            Log.e(ExceptionMessage.TAG_RECENT_SEARCH_EXCEPTION, error)
        }
    }

    override fun delete(recentSearchModel: BusRouteRecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteRecentSearch(recentSearchModel)
                loadRecentSearchContent()
            }
        }
    }

    private fun deleteRecentSearch(recentSearchModel: BusRouteRecentSearchModel) {
        val result = repository.deleteRecentSearch(recentSearchModel)

        if (result is Error) {
            error = result.message()
            Log.e(ExceptionMessage.TAG_RECENT_SEARCH_EXCEPTION, error)
        }
    }

    override fun deleteAll(context: Activity) {
        if (isContentReady(recentSearchContentLoaded)) {
            updateRecentSearchIndex(context, Const.ZERO.toLong())   // 최근 검색 인덱스 초기화
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    deleteAllRecentSearch(recentSearchContent)
                    loadRecentSearchContent()
                }
            }
        }
    }

    private fun deleteAllRecentSearch(recentSearchModels: List<BusRouteRecentSearchModel>) {
        val result = repository.deleteAllRecentSearch(recentSearchModels)

        if (result is Error) {
            error = result.message()
            Log.e(ExceptionMessage.TAG_RECENT_SEARCH_EXCEPTION, error)
        }
    }

    override fun restore() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { loadRecentSearchContent() }
        }
    }

    private fun loadRecentSearchContent() {
        val result = repository.getRecentSearch()

        if (result is Success) {
            if (result.data().isEmpty()) recentSearchContentLoaded.postValue(false)
            else {
                recentSearchContent = result.data().sortedByDescending { it.index }
                recentSearchContentLoaded.postValue(true)
            }
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.TAG_RECENT_SEARCH_EXCEPTION, error)
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
        val result = repository.getRecentSearchIndex(context)

        return (result as Success).data() + 1
    }

    private fun updateRecentSearchIndex(context: Activity, newIdx: Long) {
        val result = repository.updateRecentSearchIndex(context, newIdx)

        if (result is Error) {
            error = result.message()
            Log.e(ExceptionMessage.TAG_RECENT_SEARCH_EXCEPTION, error)
        }
    }

    private fun isContentReady(contentLoaded: MutableLiveData<Boolean>) =
        contentLoaded.isInitialized && contentLoaded.value!!
}