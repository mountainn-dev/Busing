package com.san.busing.domain.viewmodelimpl

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class SearchBusRouteViewModelImpl(private val repository: BusRouteRepositoryImpl) : SearchViewModel, ViewModel() {
    var keyword = ""
    var content = listOf<BusRouteModel>()
    private val contentReady = MutableLiveData<Boolean>()
    private var error = ""

    override val searchCompleted: LiveData<Boolean>
        get() = contentReady

    override fun search(keyword: String) {
        this.keyword = keyword

        viewModelScope.launch {
            searchBusRoutes(keyword)
        }
    }

    private suspend fun searchBusRoutes(keyword: String) {
        val result = repository.getBusRoutes(keyword)
        if (result is Success) {
            content = result.data().sortedBy { it.name }   // 노선 번호순 정렬
            contentReady.postValue(true)
        } else {
            error = (result as Error).message()
            Log.e("BusRoute Exception", error)
            contentReady.postValue(false)
        }
    }
}