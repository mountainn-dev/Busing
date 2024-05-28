package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.RecentSearchModel
import com.san.busing.domain.modelimpl.BusRouteSearchResultModelImpl
import com.san.busing.domain.modelimpl.BusRouteRecentSearchModelImpl

interface BusRepository {
    suspend fun getBusRoutes(keyword: String): Result<List<BusRouteSearchResultModelImpl>>
    suspend fun getBusRouteInfo(id: Id): Result<BusRouteInfoModel>
    fun getRecentSearch(): Result<List<BusRouteRecentSearchModelImpl>>
    fun getRecentSearchIndex(context: Activity): Result<Long>
    fun updateRecentSearchIndex(context: Activity, newIdx: Long): Result<Boolean>
    fun insertRecentSearch(recentSearchModel: RecentSearchModel): Result<Boolean>
    fun deleteRecentSearch(recentSearchModel: RecentSearchModel): Result<Boolean>
}