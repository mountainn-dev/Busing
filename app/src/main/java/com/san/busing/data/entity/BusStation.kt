package com.san.busing.data.entity

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusStationModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "busRouteStationList")
data class BusStation(
    @PropertyElement val stationId: Int,
    @PropertyElement val stationName: String,
    @PropertyElement val stationSeq: Int,
    @PropertyElement(name = "x") val positionX: Double,
    @PropertyElement(name = "y") val positionY: Double
) {
    fun toBusStationModel() = BusStationModel(
        Id(stationId),
        stationName,
        stationSeq,
        positionX,
        positionY
    )
}

data class BusStationItem(
    @Path("msgBody") @Element val item: BusStation
) {
    fun get() = item.toBusStationModel()
}
