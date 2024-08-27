package com.san.busing.view.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteRecentSearchModels
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.model.RouteSummaryModels
import com.san.busing.domain.state.UiState

interface SearchRouteViewModel {
    val state: LiveData<UiState>
    val recentSearchContentReady: LiveData<Boolean>
    var routeSummaries: RouteSummaryModels
    var routeRecentSearches: RouteRecentSearchModels
    var keyword: String
    var error: String

    fun search(keyword: String)
    fun deleteRecentSearch(itemIdx: Int)
    fun deleteAllRecentSearches(context: Activity)
    fun clearKeyword()
    fun restore()
}