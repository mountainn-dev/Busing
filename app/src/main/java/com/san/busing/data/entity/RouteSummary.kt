package com.san.busing.data.entity

import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.model.RouteSummaryModels
import com.san.busing.domain.model.StationViaRouteModel
import com.san.busing.domain.model.StationViaRouteModels
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * RouteSummary
 *
 * 노선 요약 정보를 담는 클래스
 * 노선 검색 화면과 정류장 상세 화면에서 노선 요약 정보 컨텐츠를 제공한다.
 */
@Xml(name = "busRouteList")
data class RouteSummary(
    @PropertyElement val routeId: Int,
    @PropertyElement val routeName: String,
    @PropertyElement val routeTypeCd: Int,
    @PropertyElement val routeTypeName: String,
    @PropertyElement val districtCd: Int,
    @PropertyElement val regionName: String?,
    @PropertyElement(name = "staOrder") val stationSeq: Int?
) {
    fun toRouteSummaryModel() = RouteSummaryModel(
        Id(routeId),
        Utils.getRouteType(routeTypeCd),
        routeName,
        regionName(regionName)
    )

    fun toStationViaRouteModel() = StationViaRouteModel(
        RouteSummaryModel(
            Id(routeId),
            Utils.getRouteType(routeTypeCd),
            routeName,
            regionName(regionName)
        ),
        stationSeq(stationSeq)
    )

    private fun regionName(name: String?) = when(name != null) {
        true -> name
        false -> Const.EMPTY_TEXT
    }

    private fun stationSeq(stationSeq: Int?) = when(stationSeq != null) {
        true -> stationSeq
        false -> throw Exception(ExceptionMessage.WRONG_STATION_SEQUENCE_FORMAT_EXCEPTION)
    }
}

@Xml
data class RouteSummaries(
    @Path("msgBody") @Element val routeSummaries: List<RouteSummary>
) {
    fun getAsRouteSummaryModels(): RouteSummaryModels {
        return RouteSummaryModels(routeSummaries.map { it.toRouteSummaryModel() })
    }

    fun getAsStationViaRouteModels(): StationViaRouteModels {
        return StationViaRouteModels(routeSummaries.map { it.toStationViaRouteModel() })
    }
}