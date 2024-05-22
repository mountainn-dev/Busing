package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import com.san.busing.data.entity.BusRouteRecentSearch
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusRouteRecentSearchModel

interface SearchViewModel {
    val searchResultContentReady: LiveData<Boolean>
    val recentSearchContentReady: LiveData<Boolean>
    var searchResultContent: List<BusRouteModel>
    var recentSearchContent: List<BusRouteRecentSearchModel>
    var keyword: String
    fun search(keyword: String)
    fun load()
}