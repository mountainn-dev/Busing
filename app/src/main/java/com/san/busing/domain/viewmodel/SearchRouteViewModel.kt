package com.san.busing.domain.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.state.UiState

interface SearchRouteViewModel {
    val state: LiveData<UiState>
    val recentSearchContentReady: LiveData<Boolean>
    var routeSummaries: List<RouteSummaryModel>
    var routeRecentSearches: List<RouteRecentSearchModel>
    var keyword: String
    var error: String

    fun search(keyword: String)
    fun delete(recentSearchModel: RouteRecentSearchModel)
    fun deleteAll(context: Activity)
    fun clearKeyword()
    fun restore()
}