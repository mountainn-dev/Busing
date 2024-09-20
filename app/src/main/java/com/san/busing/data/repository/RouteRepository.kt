package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.BusModels
import com.san.busing.domain.modelimpl.RouteInfoModel
import com.san.busing.domain.modelimpl.RouteRecentSearchModel
import com.san.busing.domain.modelimpl.RouteRecentSearchModels
import com.san.busing.domain.modelimpl.RouteStationModels
import com.san.busing.domain.modelimpl.RouteModels

interface RouteRepository {
    suspend fun getRouteInfo(id: Id): Result<RouteInfoModel>
    suspend fun getRoutes(keyword: String): Result<RouteModels>
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