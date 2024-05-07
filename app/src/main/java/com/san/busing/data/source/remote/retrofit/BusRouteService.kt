package com.san.busing.data.source.remote.retrofit

import com.san.busing.data.entity.BusRoute
import retrofit2.Response
import retrofit2.http.*

interface BusRouteService {
    @GET("/getBusRouteList")
    suspend fun getBusRouteList(): Response<List<BusRoute>>
}