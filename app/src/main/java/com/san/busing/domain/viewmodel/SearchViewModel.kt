package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import com.san.busing.data.entity.Test
import com.san.busing.domain.model.BusRouteModel

interface SearchViewModel {
    val searchResultContentReady: LiveData<Boolean>
    val recentSearchContentReady: LiveData<Boolean>
    var searchResultContent: List<BusRouteModel>
    var recentSearchContent: List<Test>
    var keyword: String
    fun search(keyword: String)
    fun load()
}