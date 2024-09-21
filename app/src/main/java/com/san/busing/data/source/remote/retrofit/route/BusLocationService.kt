package com.san.busing.data.source.remote.retrofit.route

import com.san.busing.data.entity.route.Buses
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BusLocationService {
    @GET("getBusLocationList")
    suspend fun getBusLocationList(
        @Query("serviceKey") key: String,
        @Query("routeId") id: Int
    ): Response<Buses>
}