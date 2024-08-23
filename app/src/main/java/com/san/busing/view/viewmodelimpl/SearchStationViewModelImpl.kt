package com.san.busing.view.viewmodelimpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.busing.data.Error
import com.san.busing.data.Success
import com.san.busing.data.repository.StationRepository
import com.san.busing.domain.model.StationSummaryModel
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.view.viewmodel.SearchStationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchStationViewModelImpl(
    private val repository: StationRepository
) : SearchStationViewModel, ViewModel() {
    override val state: LiveData<UiState>
        get() = viewModelState
    private val viewModelState = MutableLiveData<UiState>()
    override lateinit var stationSummaries: List<StationSummaryModel>

    override var keyword = Const.EMPTY_TEXT
    override lateinit var error: String
    private var searchingJob: Job? = null

    override fun search(keyword: String) {
        searchingJob?.cancel()
        this.keyword = keyword

        searchingJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchStations()
            }
        }
    }

    private suspend fun searchStations() {
        viewModelState.postValue(UiState.Loading)
        val result = repository.getStations(keyword)

        if (result is Success) {
            // 검색 결과 출력 시 노선 번호, 운행 지역 순으로 출력
            stationSummaries = result.data.sortedWith(compareBy({it.name}, {it.regionName}))
            viewModelState.postValue(UiState.Success)
        } else {
            error = (result as Error).message()
            if (result.isTimeOut()) viewModelState.postValue(UiState.Timeout)
            if (result.isCritical()) viewModelState.postValue(UiState.Error)
        }
    }

    override fun clearKeyword() {
        this.keyword = Const.EMPTY_TEXT
    }
}