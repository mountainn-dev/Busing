package com.san.busing.view.viewmodel.route

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.modelimpl.route.RouteRecentSearchModels
import com.san.busing.domain.state.UiState

interface SearchRouteViewModel {
    val state: LiveData<UiState>
    val recentSearchContentReady: LiveData<Boolean>
    var routes: RouteModels
    var routeRecentSearches: RouteRecentSearchModels
    var keyword: String
    var error: String

    fun search(keyword: String)
    fun deleteRecentSearch(itemIdx: Int)
    fun deleteAllRecentSearches(activity: Activity)
    fun clearKeyword()
    fun restore()
}