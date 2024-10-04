package com.san.busing.data.repository.station

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.modelimpl.station.BusArrivalModel
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.modelimpl.station.StationRecentSearchModel
import com.san.busing.domain.modelimpl.station.StationRecentSearchModels

interface StationRepository {
    suspend fun getStations(keyword: String): Result<StationModels>

    suspend fun getStationViaRoutes(id: Id): Result<RouteModels>

    suspend fun getBusArrival(
        stationId: Id,
        routeId: Id,
        stationSeq: Int,
    ): Result<BusArrivalModel>

    suspend fun getRecentSearch(id: Id): Result<StationRecentSearchModel>

    suspend fun getAllRecentSearch(): Result<StationRecentSearchModels>

    suspend fun insertRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>

    suspend fun updateRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>

    suspend fun deleteRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>

    suspend fun deleteAllRecentSearch(): Result<Boolean>

    fun getRecentSearchIndex(activity: Activity): Result<Long>

    fun updateRecentSearchIndex(
        activity: Activity,
        newIdx: Long,
    ): Result<Boolean>
}
