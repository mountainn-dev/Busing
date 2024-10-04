package com.san.busing.data.source.remote.retrofit.provider

import com.san.busing.BuildConfig
import com.san.busing.data.source.remote.retrofit.route.BusLocationService
import com.san.busing.data.source.remote.retrofit.route.RouteService
import com.san.busing.data.source.remote.retrofit.station.BusArrivalService
import com.san.busing.data.source.remote.retrofit.station.StationService
import retrofit2.Retrofit

object RetrofitProvider {
    private val client = ClientBuilder.get()
    private val converter = ConverterBuilder.get()
    private val routeService =
        Retrofit.Builder().baseUrl(BuildConfig.ROUTES_URL).addConverterFactory(converter)
            .client(client).build().create(RouteService::class.java)
    private val stationService =
        Retrofit.Builder().baseUrl(BuildConfig.STATION_URL).addConverterFactory(converter)
            .client(client).build().create(StationService::class.java)
    private val busLocationService =
        Retrofit.Builder().baseUrl(BuildConfig.LOCATION_URL).addConverterFactory(converter)
            .client(client).build().create(BusLocationService::class.java)
    private val busArrivalService =
        Retrofit.Builder().baseUrl(BuildConfig.ARRIVAL_URL).addConverterFactory(converter)
            .client(client).build().create(BusArrivalService::class.java)

    fun getRouteService(): RouteService = routeService

    fun getStationService(): StationService = stationService

    fun getBusLocationService(): BusLocationService = busLocationService

    fun getBusArrivalService(): BusArrivalService = busArrivalService
}
