package com.san.busing.domain.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteSummaryModel

interface SearchRouteViewModel {
    val searchResultContentReady: LiveData<Boolean>
    val recentSearchContentReady: LiveData<Boolean>
    var routeSummaries: List<RouteSummaryModel>
    var routeRecentSearches: List<RouteRecentSearchModel>
    var keyword: String
    val serviceErrorState: LiveData<Boolean>

    fun search(keyword: String)
    fun insert(recentSearchModel: RouteRecentSearchModel)
    fun update(recentSearchModel: RouteRecentSearchModel)
    fun delete(recentSearchModel: RouteRecentSearchModel)
    fun deleteAll(context: Activity)
    fun restore()
    fun clearKeyword()
    fun recentSearchIndex(context: Activity): Long
}