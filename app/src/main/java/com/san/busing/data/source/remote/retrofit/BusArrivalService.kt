package com.san.busing.data.source.remote.retrofit

import com.san.busing.data.entity.BusArrivals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BusArrivalService {
    @GET("getBusArrivalList")
    suspend fun getBusArrivalList(
        @Query("serviceKey") key: String,
        @Query("stationId") id: Int
    ): Response<BusArrivals>
}