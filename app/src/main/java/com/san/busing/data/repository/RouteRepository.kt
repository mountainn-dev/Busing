package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteInfoModel
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteStationModel

interface RouteRepository {
    suspend fun getRouteInfo(id: Id): Result<RouteInfoModel>
    suspend fun getRoutes(keyword: String): Result<List<RouteSummaryModel>>
    suspend fun getRouteStations(id: Id): Result<List<RouteStationModel>>
    suspend fun getRecentSearch(id: Id): Result<RouteRecentSearchModel>
    suspend fun getRecentSearches(): Result<List<RouteRecentSearchModel>>
    suspend fun insertRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    suspend fun updateRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    suspend fun deleteRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    suspend fun deleteAllRecentSearch(recentSearchModels: List<RouteRecentSearchModel>): Result<Boolean>
    fun getRecentSearchIndex(context: Activity): Result<Long>
    fun updateRecentSearchIndex(context: Activity, newIdx: Long): Result<Boolean>
}