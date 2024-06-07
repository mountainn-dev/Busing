package com.san.busing.data.entity

import com.san.busing.domain.model.BusModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Buses(
    @Path("msgBody") @Element val buses: List<Bus>
) {
    fun get(): List<BusModel> {
        return buses.map { it.toBusModel() }.toList()
    }
}