package com.san.busing.data.entity.route

import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.route.RouteModelImpl
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "busRouteList")
data class Route(
    @PropertyElement(name = "routeId") val id: Int,
    @PropertyElement(name = "routeTypeCd") val typeCd: Int,
    @PropertyElement(name = "routeName") val name: String,
    @PropertyElement val regionName: String?,
) {
    fun toRouteModel() = RouteModelImpl(
        Id(id),
        Utils.getRouteType(typeCd),
        name,
        regionName(regionName)
    )

    private fun regionName(name: String?) = when(name != null) {
        true -> name
        false -> Const.EMPTY_TEXT
    }
}

@Xml
data class Routes(
    @Path("msgBody") @Element val routes: List<Route>
) {
    fun get(): RouteModels {
        return RouteModels(routes.map { it.toRouteModel() })
    }
}