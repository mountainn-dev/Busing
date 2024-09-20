package com.san.busing.data.entity

import android.util.Log
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.RouteModelImpl
import com.san.busing.domain.modelimpl.RouteModels
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * StationViaRoute
 *
 * 정류장을 경유하는 노선 정보를 담는 클래스
 */
@Xml(name = "busRouteList")
data class StationViaRoute(
    @PropertyElement(name = "routeId") val id: Int,
    @PropertyElement(name = "routeTypeCd") val typeCd: Int,
    @PropertyElement(name = "routeName") val name: String,
    @PropertyElement val regionName: String?,
    @PropertyElement(name = "staOrder") val stationSeq: Int
) {
    fun toRouteModel() = RouteModelImpl(
        Id(id),
        Utils.getRouteType(typeCd),
        name,
        regionName(regionName)
    ).also { it.setStationSequence(stationSeq) }

    private fun regionName(name: String?) = when(name != null) {
        true -> name
        false -> Const.EMPTY_TEXT
    }
}

@Xml
data class StationViaRoutes(
    @Path("msgBody") @Element val stationViaRoutes: List<StationViaRoute>
) {
    fun get(): RouteModels {
        return RouteModels(stationViaRoutes.map { it.toRouteModel() })
    }
}
