package com.san.busing.domain.viewmodelimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.entity.BusRouteRecentSearch
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.type.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.viewmodel.BusRouteDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BusRouteDetailViewModelImpl(
    private val repository: BusRouteRepository,
    private val recentSearchModel: BusRouteRecentSearchModel,
) : BusRouteDetailViewModel, ViewModel() {
    private val routeInfoLoaded = MutableLiveData<Boolean>()
    private var isLoading = false
    private var error = Const.EMPTY_TEXT

    override val routeInfoReady: LiveData<Boolean>
        get() = routeInfoLoaded

    override lateinit var routeInfo: BusRouteInfoModel

    init { load() }

    override fun load() {
        if (!isLoading) {
            isLoading = true

            viewModelScope.launch {
                loadRouteInfo()
                withContext(Dispatchers.IO) { updateRecentSearch() }
                isLoading = false
            }
        }
    }

    private suspend fun loadRouteInfo() {
        val result = repository.getBusRouteInfo(recentSearchModel.id)

        if (result is Success) {
            routeInfo = result.data()
            routeInfoLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e(Const.BUS_ROUTE_INFO_EXCEPTION, error)
            routeInfoLoaded.postValue(false)
        }
    }

    private fun updateRecentSearch() {
        val result = repository.insertRecentSearch(recentSearchModel)

        if (result is Error) {
            error = result.message()
            Log.e(Const.RECENT_SEARCH_EXCEPTION, error)
        }
    }
}