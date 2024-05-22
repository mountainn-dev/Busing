package com.san.busing.data.repository

import com.san.busing.data.Result
import com.san.busing.data.entity.BusRouteRecentSearch
import com.san.busing.data.type.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusRouteRecentSearchModel

interface BusRouteRepository {
    suspend fun getBusRoutes(keyword: String): Result<List<BusRouteModel>>
    suspend fun getBusRouteInfo(id: Id): Result<BusRouteInfoModel>
    fun getRecentSearch(): Result<List<BusRouteRecentSearchModel>>
    fun insertRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean>
}