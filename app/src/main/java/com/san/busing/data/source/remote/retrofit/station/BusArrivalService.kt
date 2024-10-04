package com.san.busing.data.source.remote.retrofit.station

import com.san.busing.data.entity.station.BusArrivalItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BusArrivalService {
    @GET("getBusArrivalItem")
    suspend fun getBusArrivalItem(
        @Query("serviceKey") key: String,
        @Query("stationId") stationId: Int,
        @Query("routeId") routeId: Int,
        @Query("staOrder") stationSequence: Int,
    ): Response<BusArrivalItem>
}
