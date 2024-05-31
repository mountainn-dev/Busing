package com.san.busing.data.entity

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "busRouteInfoItem")
data class BusRoute(
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
    fun toBusRouteModel() = BusRouteModel(
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

// BusRoute Path 중복 입력을 최소화하기 위해 제작
@Xml
data class BusRouteItem(
    @Path("msgBody") @Element val item: BusRoute
) {
    fun get(): BusRouteModel {
        return item.toBusRouteModel()
    }
}
