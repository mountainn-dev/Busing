package com.san.busing.data.repository

import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModel

interface BusLocationRepository {
    suspend fun getBusLocations(id: Id): Result<List<BusModel>>
}