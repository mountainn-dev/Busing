package com.san.busing.domain.viewmodelimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.data.type.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.viewmodel.BusRouteDetailViewModel
import kotlinx.coroutines.launch

class BusRouteDetailViewModelImpl(
    private val repository: BusRouteRepository,
    private val routeId: Id
) : BusRouteDetailViewModel, ViewModel() {
    lateinit var routeInfo: BusRouteInfoModel
    private val routeInfoLoaded = MutableLiveData<Boolean>()
    private var isLoading = false
    private var error = ""

    override val routeInfoReady: LiveData<Boolean>
        get() = routeInfoLoaded

    init { load() }

    fun load() {
        if (!isLoading) {
            isLoading = true

            viewModelScope.launch {
                loadRouteInfo()
                isLoading = false
            }
        }
    }

    private suspend fun loadRouteInfo() {
        val result = repository.getBusRouteInfo(routeId)

        if (result is Success) {
            routeInfo = result.data()
            routeInfoLoaded.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e("BusRouteInfo Exception", error)
            routeInfoLoaded.postValue(false)
        }
    }
}