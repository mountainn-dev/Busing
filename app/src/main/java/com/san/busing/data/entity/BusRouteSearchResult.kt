package com.san.busing.data.entity

import com.san.busing.data.ExceptionMessage
import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType.*
import com.san.busing.domain.model.BusRouteSearchResultModel
import com.tickaroo.tikxml.annotation.*

/**
 * BusRouteSearchResult
 *
 * 버스 노선 검색 결과 목록 정보를 담는 클래스
 * 버스 노선 검색 화면 검색 결과 아이템을 구성한다.
 */
@Xml(name = "busRouteList")   // BusRoutes Element 의 자식 태그 이름과 일치화
data class BusRouteSearchResult(
    @PropertyElement val routeId: Int,
    @PropertyElement val routeName: String,
    @PropertyElement val routeTypeCd: Int,
    @PropertyElement val routeTypeName: String,
    @PropertyElement val districtCd: Int,
    @PropertyElement val regionName: String,
) {
    fun toBusRouteSearchResultModel() = BusRouteSearchResultModel(
        Id(routeId),
        routeType(routeTypeCd),
        routeName,
        regionName
    )

    private fun routeType(routeTypeCd: Int) = when (routeTypeCd) {
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
        else -> throw Exception(ExceptionMessage.NO_ROUTE_TYPE_EXCEPTION)
    }
}