package com.san.busing.data.repository

import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusArrivalModels

interface BusArrivalRepository {
    suspend fun getBusArrivals(id: Id): Result<BusArrivalModels>
}