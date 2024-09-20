package com.san.busing.view.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.modelimpl.StationRecentSearchModels
import com.san.busing.domain.modelimpl.StationModels
import com.san.busing.domain.state.UiState

interface SearchStationViewModel {
    val state: LiveData<UiState>
    val recentSearchContentReady: LiveData<Boolean>
    var stations: StationModels
    var stationRecentSearches: StationRecentSearchModels
    var keyword: String
    var error: String

    fun search(keyword: String)
    fun deleteRecentSearch(itemIdx: Int)
    fun deleteAllRecentSearches(activity: Activity)
    fun clearKeyword()
    fun restore()
}