package com.san.busing.data.entity

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteStationModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * RouteStation
 *
 * 노선 상세 화면의 정류장 아이템 정보를 담는 클래스
 * 버스 노선 상세 화면 컨텐츠를 구성한다.
 */
@Xml(name = "busRouteStationList")
data class RouteStation(
    @PropertyElement val stationId: Int,
    @PropertyElement val stationName: String,
    @PropertyElement val stationSeq: Int,
    @PropertyElement(name = "x") val positionX: Double,
    @PropertyElement(name = "y") val positionY: Double
) {
    fun toRouteStationModel() = RouteStationModel(
        Id(stationId),
        stationName,
        stationSeq,
        positionX,
        positionY
    )
}

// RouteStation Path 어노테이션 중복 입력을 최소하하기 위한 클래스
data class RouteStationItem(
    @Path("msgBody") @Element val item: RouteStation
) {
    fun get() = item.toRouteStationModel()
}

// for Service
@Xml
data class RouteStations(
    @Path("msgBody") @Element val routeStations: List<RouteStation>
) {
    fun get(): List<RouteStationModel> {
        return routeStations.map { it.toRouteStationModel() }.toList()
    }
}