package com.san.busing.view.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.StationRecentSearchModel
import com.san.busing.domain.model.StationRecentSearchModels
import com.san.busing.domain.model.StationSummaryModel
import com.san.busing.domain.model.StationSummaryModels
import com.san.busing.domain.state.UiState

interface SearchStationViewModel {
    val state: LiveData<UiState>
    val recentSearchContentReady: LiveData<Boolean>
    var stationSummaries: StationSummaryModels
    var stationRecentSearches: StationRecentSearchModels
    var keyword: String
    var error: String

    fun search(keyword: String)
    fun deleteRecentSearch(itemIdx: Int)
    fun deleteAllRecentSearches(context: Activity)
    fun clearKeyword()
    fun restore()
}