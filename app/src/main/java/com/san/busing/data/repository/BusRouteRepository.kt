package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusRouteSearchResultModel
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.model.BusStationModel

interface BusRouteRepository {
    suspend fun getBusRoute(id: Id): Result<BusRouteModel>
    suspend fun getBusRoutes(keyword: String): Result<List<BusRouteSearchResultModel>>
    suspend fun getBusStations(id: Id): Result<List<BusStationModel>>
    fun getRecentSearch(): Result<List<BusRouteRecentSearchModel>>
    fun insertRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean>
    fun updateRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean>
    fun deleteRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean>
    fun deleteAllRecentSearch(recentSearchModels: List<BusRouteRecentSearchModel>): Result<Boolean>
    fun getRecentSearchIndex(context: Activity): Result<Long>
    fun updateRecentSearchIndex(context: Activity, newIdx: Long): Result<Boolean>
}