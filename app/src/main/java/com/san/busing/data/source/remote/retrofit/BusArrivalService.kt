package com.san.busing.data.source.remote.retrofit

import com.san.busing.data.entity.BusArrival
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

    @GET("getBusArrivalItem")
    suspend fun getBusArrivalItem(
        @Query("serviceKey") key: String,
        @Query("stationId") stationId: Int,
        @Query("routeId") routeId: Int,
        @Query("staOrder") stationSequence: Int
    ): Response<BusArrival>
}