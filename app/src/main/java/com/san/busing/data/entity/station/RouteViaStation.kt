package com.san.busing.data.entity.station

import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.station.StationModelImpl
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.utils.Const
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * RouteViaStation
 *
 * 노선이 경유하는 정류장 정보를 담는 클래스
 */
@Xml(name = "busRouteStationList")
data class RouteViaStation(
    @PropertyElement(name = "stationId") val id: Int,
    @PropertyElement val mobileNo: String?,
    @PropertyElement(name = "stationName") val name: String,
    @PropertyElement val regionName: String?,
    @PropertyElement val stationSeq: Int,
    @PropertyElement val turnYn: String,
) {
    fun toStationModel() = StationModelImpl(
        Id(id),
        mobileNo(mobileNo),
        name,
        regionName(regionName)
    ).also {
        it.setStationSequence(stationSeq)
        it.setIsTurnaround(isTurnaround(turnYn))
    }

    private fun mobileNo(mobileNo: String?) = when(mobileNo.isNullOrBlank()) {
        true -> NO_MOBILE_NUMBER
        false -> mobileNo
    }

    private fun regionName(name: String?) = when(name != null) {
        true -> name
        false -> Const.EMPTY_TEXT
    }

    private fun isTurnaround(turnYn: String) = when(turnYn) {
        NOT_TURNAROUND -> false
        TURNAROUND -> true
        else -> throw Exception(ExceptionMessage.WRONG_TURNAROUND_VALUE_EXCEPTION)
    }

    companion object {
        private const val NO_MOBILE_NUMBER = "00000"
        private const val NOT_TURNAROUND = "N"
        private const val TURNAROUND = "Y"
    }
}

@Xml
data class RouteViaStations(
    @Path("msgBody") @Element val routeViaStations: List<RouteViaStation>
) {
    fun get(): StationModels {
        return StationModels(routeViaStations.map { it.toStationModel() })
    }
}