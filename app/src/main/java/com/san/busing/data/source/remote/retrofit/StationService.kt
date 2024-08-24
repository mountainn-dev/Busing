package com.san.busing.data.source.remote.retrofit

import com.san.busing.data.entity.StationSummaries
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StationService {
    @GET("getBusStationList")
    suspend fun getBusStationList(
        @Query("serviceKey") key: String,
        @Query("keyword") keyword: String
    ): Response<StationSummaries>
}