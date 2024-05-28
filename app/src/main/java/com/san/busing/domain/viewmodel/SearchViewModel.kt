package com.san.busing.domain.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.model.BusRouteSearchResultModel

interface SearchViewModel {
    val searchResultContentReady: LiveData<Boolean>
    val recentSearchContentReady: LiveData<Boolean>
    var searchResultContent: List<BusRouteSearchResultModel>
    var recentSearchContent: List<BusRouteRecentSearchModel>
    var keyword: String
    fun search(keyword: String)
    fun loadContent()
    fun recentSearchIndex(context: Activity): Long
    fun delete(recentSearchModel: BusRouteRecentSearchModel)
}