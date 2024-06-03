package com.san.busing.data.entity

import com.san.busing.domain.model.BusRouteSearchResultModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class BusRoutes(
    @Path("msgBody") @Element val busRoutes: List<BusRouteSearchResult>
) {
    fun get(): List<BusRouteSearchResultModel> {
        return busRoutes.map { it.toBusRouteSearchResultModel() }.toList()
    }
}
