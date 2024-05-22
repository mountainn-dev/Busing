package com.san.busing.domain.viewmodelimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.entity.Test
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchBusRouteViewModelImpl(
    private val repository: BusRouteRepository
) : SearchViewModel, ViewModel() {
    private val searchResultContentLoaded = MutableLiveData<Boolean>()
    private val recentSearchContentLoaded = MutableLiveData<Boolean>()
    private var isSearching = false
    private var error = ""

    override val searchResultContentReady: LiveData<Boolean>
        get() = searchResultContentLoaded
    override val recentSearchContentReady: LiveData<Boolean>
        get() = recentSearchContentLoaded

    override var searchResultContent = listOf<BusRouteModel>()

    override var recentSearchContent = listOf<Test>()

    override var keyword = ""

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
            Log.e("BusRoute Exception", error)
            searchResultContentLoaded.postValue(false)
        }
    }

    override fun load() {
        Log.d("load", "start")
        viewModelScope.launch {
            withContext(Dispatchers.IO) { loadRecentSearchContent() }
        }
    }

    private fun loadRecentSearchContent() {
        val result = repository.getTest()

        if (result is Success) {
            recentSearchContent = result.data()
            recentSearchContentLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e("Recent Search Exception", error)
            recentSearchContentLoaded.postValue(false)
        }
    }
}