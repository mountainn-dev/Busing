package com.san.busing.domain.viewmodelimpl

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
    private val contentReady = MutableLiveData<Boolean>()
    var content = listOf<BusRouteModel>()
    var error = ""

    override val searchCompleted: LiveData<Boolean>
        get() = contentReady

    override fun search(keyword: String) {
        viewModelScope.launch {
            val result = repository.getBusRoutes(keyword)
            if (result is Success) {
                content = (result).data()
                contentReady.postValue(true)
            } else {
                error = (result as Error).message()
                contentReady.postValue(false)
            }
        }
    }
}