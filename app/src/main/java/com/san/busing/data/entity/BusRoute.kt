package com.san.busing.data.entity

import com.san.busing.domain.model.BusRouteModel

data class BusRoute(
    private val routeId: Id,
    private val routeName: String,
    private val routeTypeCd: CodeNumber,
    private val routeTypeName: String,
    private val regionName: String,
    private val districtCd: CodeNumber
) {
    fun toBusRouteModel() = BusRouteModel(
        routeId,
        routeName,
        routeTypeCd,
        routeTypeName,
        regionName
    )
}