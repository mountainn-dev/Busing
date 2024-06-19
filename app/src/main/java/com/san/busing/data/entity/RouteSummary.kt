package com.san.busing.data.entity

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.utils.Utils
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * RouteSummary
 *
 * 버스 노선 검색 시 노선 요약 정보를 담는 클래스
 * 버스 노선 검색 화면 검색 결과를 구성한다.
 */
@Xml(name = "busRouteList")
data class RouteSummary(
    @PropertyElement val routeId: Int,
    @PropertyElement val routeName: String,
    @PropertyElement val routeTypeCd: Int,
    @PropertyElement val routeTypeName: String,
    @PropertyElement val districtCd: Int,
    @PropertyElement val regionName: String,
) {
    fun toBusRouteSearchResultModel() = RouteSummaryModel(
        Id(routeId),
        Utils.getRouteType(routeTypeCd),
        routeName,
        regionName
    )
}

// for Service
@Xml
data class RouteSummaries(
    @Path("msgBody") @Element val routeSummaries: List<RouteSummary>
) {
    fun get(): List<RouteSummaryModel> {
        return routeSummaries.map { it.toBusRouteSearchResultModel() }.toList()
    }
}