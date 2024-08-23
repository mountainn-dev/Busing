package com.san.busing.data.repository

import com.san.busing.data.Result
import com.san.busing.domain.model.StationSummaryModel

interface StationRepository {
    suspend fun getStations(keyword: String): Result<List<StationSummaryModel>>
}