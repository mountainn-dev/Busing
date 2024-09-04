package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModels
import com.san.busing.domain.model.RouteInfoModel
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteRecentSearchModels
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.model.RouteStationModels
import com.san.busing.domain.model.RouteSummaryModels

interface RouteRepository {
    suspend fun getRouteInfo(id: Id): Result<RouteInfoModel>
    suspend fun getRoutes(keyword: String): Result<RouteSummaryModels>
    suspend fun getRouteStations(id: Id): Result<RouteStationModels>
    suspend fun getBusLocations(id: Id): Result<BusModels>
    suspend fun getRecentSearch(id: Id): Result<RouteRecentSearchModel>
    suspend fun getAllRecentSearch(): Result<RouteRecentSearchModels>
    suspend fun insertRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    suspend fun updateRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    suspend fun deleteRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>
    suspend fun deleteAllRecentSearch(): Result<Boolean>
    fun getRecentSearchIndex(activity: Activity): Result<Long>
    fun updateRecentSearchIndex(activity: Activity, newIdx: Long): Result<Boolean>
}