package com.san.busing.data.repository

import com.san.busing.data.Result
import com.san.busing.data.entity.Test
import com.san.busing.data.type.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.BusRouteModel

interface BusRouteRepository {
    suspend fun getBusRoutes(keyword: String): Result<List<BusRouteModel>>
    suspend fun getBusRouteInfo(id: Id): Result<BusRouteInfoModel>
    fun getTest(): Result<List<Test>>

    fun insertRecentSearch(test: Test): Result<Boolean>
}