package com.san.busing.data.entity

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteInfoModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * RouteInfo
 *
 * 특정 노선의 상세 정보를 담는 클래스
 * 버스 노선 상세 화면 컨텐츠를 구성한다.
 */
@Xml(name = "busRouteInfoItem")
data class RouteInfo(
    // up - 기점, down - 종점
    @PropertyElement val routeId: Int,
    @PropertyElement val routeName: String,
    @PropertyElement val routeTypeCd: Int,
    @PropertyElement val routeTypeName: String,
    @PropertyElement val startStationId: Int,
    @PropertyElement val startStationName: String,
    @PropertyElement val endStationId: Int,
    @PropertyElement val endStationName: String,
//    @PropertyElement(name = "upFirstTime") val startFirstTime: String,
//    @PropertyElement(name = "upLastTime") val startLastTime: String,
//    @PropertyElement(name = "downFirstTime") val finishFirstTime: String,
//    @PropertyElement(name = "downLastTime") val finishLastTime: String,
//    @PropertyElement(name = "nPeekAlloc") val maxPeekAlloc: Int
) {
    fun toRouteInfoModel() = RouteInfoModel(
        Id(routeId),
        routeName,
        Id(startStationId),
        startStationName,
        Id(endStationId),
        endStationName,
//        Time(startFirstTime),
//        Time(startLastTime),
//        Time(finishFirstTime),
//        Time(finishLastTime),
//        Time.minute(maxPeekAlloc)
    )
}

// RouteInfo Path 어노테이션 중복 입력을 최소화하기 위한 클래스
@Xml
data class RouteInfoItem(
    @Path("msgBody") @Element val item: RouteInfo
) {
    fun get(): RouteInfoModel {
        return item.toRouteInfoModel()
    }
}