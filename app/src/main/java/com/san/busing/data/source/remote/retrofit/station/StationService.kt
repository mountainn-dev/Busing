package com.san.busing.data.source.remote.retrofit.station

import com.san.busing.data.entity.route.StationViaRoutes
import com.san.busing.data.entity.station.Stations
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StationService {
    @GET("getBusStationList")
    suspend fun getBusStationList(
        @Query("serviceKey") key: String,
        @Query("keyword") keyword: String
    ): Response<Stations>

    @GET("getBusStationViaRouteList")
    suspend fun getBusStationViaRouteList(
        @Query("serviceKey") key: String,
        @Query("stationId") id: Int
    ): Response<StationViaRoutes>
}