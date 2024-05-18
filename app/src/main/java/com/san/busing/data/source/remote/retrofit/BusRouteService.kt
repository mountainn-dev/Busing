package com.san.busing.data.source.remote.retrofit

import com.san.busing.data.entity.BusRouteInfoItem
import com.san.busing.data.entity.BusRoutes
import retrofit2.Response
import retrofit2.http.*

interface BusRouteService {
    @GET("getBusRouteList")
    suspend fun getBusRouteList(
        @Query("serviceKey") key: String,
        @Query("keyword") keyword: String
    ): Response<BusRoutes>

    @GET("getBusRouteInfoItem")
    suspend fun getBusRouteInfoItem(
        @Query("serviceKey") key: String,
        @Query("routeId") id: Int
    ): Response<BusRouteInfoItem>
}