package com.san.busing.domain.utils

import com.san.busing.R
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.source.remote.interceptor.ErrorInterceptor
import com.san.busing.domain.enums.PlateType.*
import com.san.busing.domain.enums.RouteType.*
import com.san.busing.data.source.remote.retrofit.ServiceResult.*
import com.san.busing.domain.enums.RouteType
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object Utils {
    fun getRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(TikXmlConverterFactory.create(getXmlParse()))
        .client(getClient())
        .build()

    private fun getClient() = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(ErrorInterceptor())
        .build()

    private fun getXmlParse() = TikXml.Builder().exceptionOnUnreadXml(false).build()
    fun getServiceResult(resultCd: Int) = when(resultCd) {
        SUCCESS.code -> SUCCESS
        SYSTEM_ERROR.code -> SYSTEM_ERROR
        NO_ESSENTIAL_PARAMETER.code -> NO_ESSENTIAL_PARAMETER
        WRONG_ESSENTIAL_PARAMETER.code -> WRONG_ESSENTIAL_PARAMETER
        NO_RESULT.code -> NO_RESULT
        NO_SERVICE_KEY.code -> NO_SERVICE_KEY
        WRONG_SERVICE_KEY.code -> WRONG_SERVICE_KEY
        UNAUTHORIZED_SERVICE_KEY.code -> UNAUTHORIZED_SERVICE_KEY
        OVER_REQUEST_LIMIT.code -> OVER_REQUEST_LIMIT
        WRONG_POSITION_REQUEST.code -> WRONG_POSITION_REQUEST
        WRONG_FORMAT_ROUTE_NAME.code -> WRONG_FORMAT_ROUTE_NAME
        WRONG_FORMAT_STATION_NAME.code -> WRONG_FORMAT_STATION_NAME
        NO_RESULT_BUS_ARRIVAL.code -> NO_RESULT_BUS_ARRIVAL
        WRONG_START_STATION_ID.code -> WRONG_START_STATION_ID
        WRONG_ARRIVAL_STATION_ID.code -> WRONG_ARRIVAL_STATION_ID
        SERVICE_NOT_READY.code -> SERVICE_NOT_READY
        else -> throw NoSuchElementException(ExceptionMessage.NO_SERVICE_RESULT)
    }
    fun getRouteType(routeTypeCd: Int) = when(routeTypeCd) {
        AIRPORT_NORMAL.code -> AIRPORT_NORMAL
        AIRPORT_LIMO.code -> AIRPORT_LIMO
        AIRPORT_SEAT.code -> AIRPORT_SEAT
        AREA_DIRECT.code -> AREA_DIRECT
        AREA_EXPRESS.code -> AREA_EXPRESS
        CIRCULAR.code -> CIRCULAR
        NORMAL.code -> NORMAL
        NORMAL_SEAT.code -> NORMAL_SEAT
        OUT_TOWN_NORMAL.code -> OUT_TOWN_NORMAL
        OUT_TOWN_EXPRESS.code -> OUT_TOWN_EXPRESS
        OUT_TOWN_SEAT.code -> OUT_TOWN_SEAT
        RURAL_NORMAL.code -> RURAL_NORMAL
        RURAL_DIRECT.code -> RURAL_DIRECT
        RURAL_SEAT.code -> RURAL_SEAT
        VILLAGE.code -> VILLAGE
        else -> throw NoSuchElementException(ExceptionMessage.NO_ROUTE_TYPE_EXCEPTION)
    }
    fun getPlateType(plateType: Int) = when(plateType) {
        NONE.code -> NONE
        COMPACT.code -> COMPACT
        MID_SIZE.code -> MID_SIZE
        FULL_SIZE.code -> FULL_SIZE
        DOUBLE_DECKER.code -> DOUBLE_DECKER
        else -> throw NoSuchElementException(ExceptionMessage.NO_PLATE_TYPE_EXCEPTION)
    }

    fun getColorByRouteType(type: RouteType) = when (type) {
        AIRPORT_NORMAL, AIRPORT_LIMO, AIRPORT_SEAT, CIRCULAR -> R.color.deep_blue
        NORMAL, NORMAL_SEAT, OUT_TOWN_NORMAL, OUT_TOWN_EXPRESS, OUT_TOWN_SEAT -> R.color.blue
        AREA_EXPRESS, AREA_DIRECT -> R.color.red
        RURAL_NORMAL, RURAL_DIRECT, RURAL_SEAT, VILLAGE -> R.color.green
    }

    fun getLightColorByRouteType(type: RouteType) = when (type) {
        AIRPORT_NORMAL, AIRPORT_LIMO, AIRPORT_SEAT, CIRCULAR -> R.color.light_deep_blue
        NORMAL, NORMAL_SEAT, OUT_TOWN_NORMAL, OUT_TOWN_EXPRESS, OUT_TOWN_SEAT -> R.color.light_blue
        AREA_EXPRESS, AREA_DIRECT -> R.color.light_red
        RURAL_NORMAL, RURAL_DIRECT, RURAL_SEAT, VILLAGE -> R.color.light_green
    }

    fun getBusImageResourceByRouteType(type: RouteType) = when (type) {
        AIRPORT_NORMAL, AIRPORT_LIMO, AIRPORT_SEAT, CIRCULAR -> R.drawable.ic_bus_deep_blue
        NORMAL, NORMAL_SEAT, OUT_TOWN_NORMAL, OUT_TOWN_EXPRESS, OUT_TOWN_SEAT -> R.drawable.ic_bus_blue
        AREA_EXPRESS, AREA_DIRECT -> R.drawable.ic_bus_red
        RURAL_NORMAL, RURAL_DIRECT, RURAL_SEAT, VILLAGE -> R.drawable.ic_bus_green
    }
}