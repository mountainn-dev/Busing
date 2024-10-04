package com.san.busing.data.repository.route

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.route.BusModels
import com.san.busing.domain.modelimpl.route.RouteInfoModel
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.modelimpl.route.RouteRecentSearchModel
import com.san.busing.domain.modelimpl.route.RouteRecentSearchModels
import com.san.busing.domain.modelimpl.station.StationModels

interface RouteRepository {
    suspend fun getRouteInfo(id: Id): Result<RouteInfoModel>

    suspend fun getRoutes(keyword: String): Result<RouteModels>

    suspend fun getRouteStations(id: Id): Result<StationModels>

    suspend fun getBusLocations(id: Id): Result<BusModels>

    suspend fun getRecentSearch(id: Id): Result<RouteRecentSearchModel>

    suspend fun getAllRecentSearch(): Result<RouteRecentSearchModels>

    suspend fun insertRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>

    suspend fun updateRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>

    suspend fun deleteRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean>

    suspend fun deleteAllRecentSearch(): Result<Boolean>

    fun getRecentSearchIndex(activity: Activity): Result<Long>

    fun updateRecentSearchIndex(
        activity: Activity,
        newIdx: Long,
    ): Result<Boolean>
}
