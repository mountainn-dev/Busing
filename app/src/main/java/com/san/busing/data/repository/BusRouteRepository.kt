package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusRouteSearchResultModel
import com.san.busing.domain.model.BusRouteRecentSearchModel

interface BusRouteRepository {
    suspend fun getBusRoute(id: Id): Result<BusRouteModel>
    suspend fun getBusRoutes(keyword: String): Result<List<BusRouteSearchResultModel>>
    fun getRecentSearch(): Result<List<BusRouteRecentSearchModel>>
    fun insertRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean>
    fun deleteRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean>
    fun getRecentSearchIndex(context: Activity): Result<Long>
    fun updateRecentSearchIndex(context: Activity, newIdx: Long): Result<Boolean>
}