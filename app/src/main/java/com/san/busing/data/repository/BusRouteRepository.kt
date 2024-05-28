package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.BusRouteSearchResultModel
import com.san.busing.domain.model.BusRouteRecentSearchModel

interface BusRouteRepository {
    suspend fun getBusRoutes(keyword: String): Result<List<BusRouteSearchResultModel>>
    suspend fun getBusRouteInfo(id: Id): Result<BusRouteInfoModel>
    fun getRecentSearch(): Result<List<BusRouteRecentSearchModel>>
    fun getRecentSearchIndex(context: Activity): Result<Long>
    fun updateRecentSearchIndex(context: Activity, newIdx: Long): Result<Boolean>
    fun insertRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean>
    fun deleteRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean>
}