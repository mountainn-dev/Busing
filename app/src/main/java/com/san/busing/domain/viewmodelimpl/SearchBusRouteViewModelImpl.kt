package com.san.busing.domain.viewmodelimpl

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.ExceptionMessage
import com.san.busing.data.Success
import com.san.busing.data.repository.BusRepository
import com.san.busing.domain.model.RecentSearchModel
import com.san.busing.domain.model.SearchResultModel
import com.san.busing.domain.modelimpl.BusRouteRecentSearchModelImpl
import com.san.busing.domain.modelimpl.BusRouteSearchResultModelImpl
import com.san.busing.domain.utils.Const
import com.san.busing.domain.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchBusRouteViewModelImpl(
    private val repository: BusRepository
) : SearchViewModel, ViewModel() {
    private val searchResultContentLoaded = MutableLiveData<Boolean>()
    private val recentSearchContentLoaded = MutableLiveData<Boolean>()
    private var isSearching = false
    private var error = Const.EMPTY_TEXT

    override val searchResultContentReady: LiveData<Boolean>
        get() = searchResultContentLoaded
    override val recentSearchContentReady: LiveData<Boolean>
        get() = recentSearchContentLoaded

    override var searchResultContent: List<SearchResultModel> = listOf<BusRouteSearchResultModelImpl>()

    override var recentSearchContent: List<RecentSearchModel> = listOf<BusRouteRecentSearchModelImpl>()

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
            Log.e(ExceptionMessage.BUS_ROUTE_EXCEPTION, error)
            searchResultContentLoaded.postValue(false)
        }
    }

    /**
     * fun load(): void
     *
     * HomeActivity 프레그먼트 탭 전환 혹은 BusRouteInfo 확인 이후 프레그먼트가 재개될 때 실행
     */
    override fun loadContent() {
        loadSearchResultContent()
        viewModelScope.launch {
            withContext(Dispatchers.IO) { loadRecentSearchContent() }
        }
    }

    private fun loadSearchResultContent() {
        if (isSearchResultUsable()) searchResultContentLoaded.postValue(true)
        else searchResultContentLoaded.postValue(false)
    }

    private fun isSearchResultUsable() =
        searchResultContentLoaded.isInitialized && searchResultContentLoaded.value!!

    private fun loadRecentSearchContent() {
        val result = repository.getRecentSearch()

        if (result is Success) {
            recentSearchContent = result.data().sortedByDescending { it.index }
            recentSearchContentLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(ExceptionMessage.RECENT_SEARCH_EXCEPTION, error)
            recentSearchContentLoaded.postValue(false)
        }
    }

    override fun updateRecentSearch(recentSearchModel: RecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { update(recentSearchModel) }
        }
    }

    private fun update(recentSearchModel: RecentSearchModel) {
        val result = repository.insertRecentSearch(recentSearchModel)

        if (result is Error) {
            error = result.message()
            Log.e(ExceptionMessage.RECENT_SEARCH_EXCEPTION, error)
        }
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
            Log.e(ExceptionMessage.RECENT_SEARCH_EXCEPTION, error)
        }
    }

    override fun delete(recentSearchModel: RecentSearchModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                deleteRecentSearch(recentSearchModel)
                loadRecentSearchContent()
            }
        }
    }

    private fun deleteRecentSearch(recentSearchModel: RecentSearchModel) {
        val result = repository.deleteRecentSearch(recentSearchModel)

        if (result is Error) {
            error = result.message()
            Log.e(ExceptionMessage.RECENT_SEARCH_EXCEPTION, error)
        }
    }
}