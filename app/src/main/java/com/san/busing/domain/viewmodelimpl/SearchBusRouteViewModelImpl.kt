package com.san.busing.domain.viewmodelimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.domain.model.BusRouteItemModel
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.viewmodel.SearchViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.util.LinkedList

class SearchBusRouteViewModelImpl(private val repository: BusRouteRepositoryImpl) : SearchViewModel, ViewModel() {
    var keyword = ""
    var itemContent = listOf<BusRouteItemModel>()
    private var routes = listOf<BusRouteModel>()
    private val contentReady = MutableLiveData<Boolean>()
    private var error = ""
    private var isViewModelOnError = false

    override val searchCompleted: LiveData<Boolean>
        get() = contentReady

    override fun search(keyword: String) {
        this.keyword = keyword
        isViewModelOnError = false

        val contents = mutableListOf<BusRouteItemModel>()
        viewModelScope.launch {
            searchBusRoutes(keyword)
            if (!isViewModelOnError) {
                routes.map { async {searchBusRouteInfo(it, contents) } }.awaitAll()
            }
            if (!isViewModelOnError) {
                itemContent = contents
                contentReady.postValue(true)
            }
        }
    }

    private suspend fun searchBusRoutes(keyword: String) {
        val result = repository.getBusRoutes(keyword)
        if (result is Success) {
            routes = result.data()
        } else {
            error = (result as Error).message()
            isViewModelOnError = true
            Log.e("BusRoute Exception", error)
        }
    }

    private suspend fun searchBusRouteInfo(route: BusRouteModel, contents: MutableList<BusRouteItemModel>) {
        val result = repository.getBusRouteInfo(route.id)
        if (result is Success) {
            contents.add(BusRouteItemModel(route, result.data()))
        } else {
            error = (result as Error).message()
            isViewModelOnError = true
            Log.e("BusRouteInfo Exception", error)
        }
    }
}