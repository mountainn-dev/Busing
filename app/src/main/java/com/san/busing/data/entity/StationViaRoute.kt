package com.san.busing.data.entity

import com.san.busing.domain.model.StationViaRouteModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "busRouteList")
data class StationViaRoute(
    @Element val routeSummary: RouteSummary,
    @PropertyElement val stationSeq: Int
) {
    fun toStationViaRouteModel() = StationViaRouteModel(
        routeSummary.toRouteSummaryModel(),
        stationSeq
    )
}

data class StationViaRoutes(
    @Path("msgBody") @Element val stationViaRoutes: List<StationViaRoute>
) {
    fun get(): List<StationViaRouteModel> {
        return stationViaRoutes.map { it.toStationViaRouteModel() }.toList()
    }
}
