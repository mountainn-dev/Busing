package com.san.busing.data.source.remote.retrofit.route

import com.san.busing.data.entity.route.RouteInfoItem
import com.san.busing.data.entity.route.Routes
import com.san.busing.data.entity.station.RouteViaStations
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RouteService {
    @GET("getBusRouteList")
    suspend fun getBusRouteList(
        @Query("serviceKey") key: String,
        @Query("keyword") keyword: String
    ): Response<Routes>

    @GET("getBusRouteInfoItem")
    suspend fun getBusRouteInfoItem(
        @Query("serviceKey") key: String,
        @Query("routeId") id: Int
    ): Response<RouteInfoItem>

    @GET("getBusRouteStationList")
    suspend fun getBusStationList(
        @Query("serviceKey") key: String,
        @Query("routeId") id: Int
    ): Response<RouteViaStations>
}