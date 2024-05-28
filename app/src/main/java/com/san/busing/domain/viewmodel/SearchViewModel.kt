package com.san.busing.domain.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import com.san.busing.domain.model.RecentSearchModel
import com.san.busing.domain.model.SearchResultModel
import com.san.busing.domain.modelimpl.BusRouteRecentSearchModelImpl
import com.san.busing.domain.modelimpl.BusRouteSearchResultModelImpl

interface SearchViewModel {
    val searchResultContentReady: LiveData<Boolean>
    val recentSearchContentReady: LiveData<Boolean>
    var searchResultContent: List<SearchResultModel>
    var recentSearchContent: List<RecentSearchModel>
    var keyword: String
    fun search(keyword: String)
    fun loadContent()
    fun updateRecentSearch(recentSearchModel: RecentSearchModel)
    fun recentSearchIndex(context: Activity): Long
    fun delete(recentSearchModel: RecentSearchModel)
}