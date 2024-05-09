package com.san.busing.data.entity

import com.san.busing.domain.model.BusRouteModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class BusRoutes(
    @Path("msgBody") @Element val busRoutes: List<BusRoute>
) {
    fun get(): List<BusRouteModel> {
        return busRoutes.map { it.toBusRouteModel() }.toList()
    }
}
