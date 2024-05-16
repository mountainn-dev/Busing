package com.san.busing.data.entity

import com.san.busing.data.type.CodeNumber
import com.san.busing.data.type.Id
import com.san.busing.domain.model.BusRouteModel
import com.tickaroo.tikxml.annotation.*

@Xml(name = "busRouteList")   // BusRoutes Element 의 자식 태그 이름과 일치화
data class BusRoute(
    @PropertyElement val routeId: Int,
    @PropertyElement val routeName: String,
    @PropertyElement val routeTypeCd: Int,
    @PropertyElement val routeTypeName: String,
    @PropertyElement val districtCd: Int,
    @PropertyElement val regionName: String,
) {
    fun toBusRouteModel() = BusRouteModel(
        Id(routeId),
        routeName,
        CodeNumber(routeTypeCd),
        routeTypeName,
        regionName
    )
}

@Xml
data class BusRoutes(
    @Path("msgBody") @Element val busRoutes: List<BusRoute>
) {
    fun get(): List<BusRouteModel> {
        return busRoutes.map { it.toBusRouteModel() }.toList()
    }
}