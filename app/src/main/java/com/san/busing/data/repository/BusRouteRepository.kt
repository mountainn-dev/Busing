package com.san.busing.data.repository

import com.san.busing.domain.model.BusRouteModel

interface BusRouteRepository {
    suspend fun getBusRoutes(): Result<List<BusRouteModel>>
}