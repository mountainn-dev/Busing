package com.san.busing.data.repository

import com.san.busing.data.Result
import com.san.busing.domain.model.BusRouteModel

interface BusRouteRepository {
    suspend fun getBusRoutes(keyword: String): Result<List<BusRouteModel>>
}