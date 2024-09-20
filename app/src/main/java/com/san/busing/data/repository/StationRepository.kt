package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.BusArrivalModel
import com.san.busing.domain.modelimpl.RouteModels
import com.san.busing.domain.modelimpl.StationRecentSearchModel
import com.san.busing.domain.modelimpl.StationRecentSearchModels
import com.san.busing.domain.modelimpl.StationSummaryModels

interface StationRepository {
    suspend fun getStations(keyword: String): Result<StationSummaryModels>
    suspend fun getStationViaRoutes(id: Id): Result<RouteModels>
    suspend fun getBusArrival(stationId: Id, routeId: Id, stationSeq: Int): Result<BusArrivalModel>
    suspend fun getRecentSearch(id: Id): Result<StationRecentSearchModel>
    suspend fun getAllRecentSearch(): Result<StationRecentSearchModels>
    suspend fun insertRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>
    suspend fun updateRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>
    suspend fun deleteRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>
    suspend fun deleteAllRecentSearch(): Result<Boolean>
    fun getRecentSearchIndex(activity: Activity): Result<Long>
    fun updateRecentSearchIndex(activity: Activity, newIdx: Long): Result<Boolean>
}