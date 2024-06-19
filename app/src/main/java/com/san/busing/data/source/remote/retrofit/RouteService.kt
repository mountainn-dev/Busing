package com.san.busing.data.source.remote.retrofit

import com.san.busing.data.entity.RouteInfoItem
import com.san.busing.data.entity.RouteSummaries
import com.san.busing.data.entity.RouteStations
import retrofit2.Response
import retrofit2.http.*

interface RouteService {
    @GET("getBusRouteList")
    suspend fun getBusRouteList(
        @Query("serviceKey") key: String,
        @Query("keyword") keyword: String
    ): Response<RouteSummaries>

    @GET("getBusRouteInfoItem")
    suspend fun getBusRouteInfoItem(
        @Query("serviceKey") key: String,
        @Query("routeId") id: Int
    ): Response<RouteInfoItem>

    @GET("getBusRouteStationList")
    suspend fun getBusStationList(
        @Query("serviceKey") key: String,
        @Query("routeId") id: Int
    ): Response<RouteStations>
}