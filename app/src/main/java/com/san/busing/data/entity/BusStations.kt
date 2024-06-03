package com.san.busing.data.entity

import com.san.busing.domain.model.BusStationModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class BusStations(
    @Path("msgBody") @Element val busStations: List<BusStation>
) {
    fun get(): List<BusStationModel> {
        return busStations.map { it.toBusStationModel() }.toList()
    }
}
