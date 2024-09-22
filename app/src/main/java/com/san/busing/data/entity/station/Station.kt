package com.san.busing.data.entity.station

import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.station.StationModelImpl
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.utils.Const
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "busStationList")
data class Station(
    @PropertyElement(name = "stationId") val id: Int,
    @PropertyElement val mobileNo: String?,
    @PropertyElement(name = "stationName") val name: String,
    @PropertyElement val regionName: String?,
) {
    fun toStationModel() = StationModelImpl(
        Id(id),
        mobileNo(mobileNo),
        name,
        regionName(regionName)
    )

    private fun mobileNo(mobileNo: String?) = when(mobileNo.isNullOrBlank()) {
        true -> NO_MOBILE_NUMBER
        false -> mobileNo
    }

    private fun regionName(name: String?) = when(name != null) {
        true -> name
        false -> Const.EMPTY_TEXT
    }

    companion object {
        private const val NO_MOBILE_NUMBER = "00000"
    }
}

@Xml
data class Stations(
    @Path("msgBody") @Element val stations: List<Station>
) {
    fun get(): StationModels {
        return StationModels(stations.map { it.toStationModel() })
    }
}