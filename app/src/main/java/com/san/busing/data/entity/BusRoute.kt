package com.san.busing.data.entity

import com.san.busing.domain.model.BusRouteModel
import com.tickaroo.tikxml.annotation.*

@Xml
data class BusRoute(
    @PropertyElement val routeId: Int,
    @PropertyElement val routeName: String,
    @PropertyElement val routeTypeCd: Int,
    @PropertyElement val routeTypeName: String,
    @PropertyElement val regionName: String,
    @PropertyElement val districtCd: Int
) {
    fun toBusRouteModel() = BusRouteModel(
        Id(routeId),
        routeName,
        CodeNumber(routeTypeCd),
        routeTypeName,
        regionName
    )
}

