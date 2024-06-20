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
    fun getRecentSearch(): Result<List<RouteRecentSearchModel>>
    fun insertRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    fun updateRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    fun deleteRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    fun deleteAllRecentSearch(recentSearchModels: List<RouteRecentSearchModel>): Result<Boolean>
    fun getRecentSearchIndex(context: Activity): Result<Long>
    fun updateRecentSearchIndex(context: Activity, newIdx: Long): Result<Boolean>
}