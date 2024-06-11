package com.san.busing.data.entity

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteSearchResultModel
import com.san.busing.domain.utils.Utils
import com.tickaroo.tikxml.annotation.*

/**
 * BusRouteSearchResult
 *
 * 버스 노선 검색 결과 목록 정보를 담는 클래스
 * 버스 노선 검색 화면 검색 결과 아이템을 구성한다.
 */
@Xml(name = "busRouteList")   // BusRoutes Element 의 자식 태그 이름과 일치화
data class BusRouteSearchResult(
    @PropertyElement val routeId: Int,
    @PropertyElement val routeName: String,
    @PropertyElement val routeTypeCd: Int,
    @PropertyElement val routeTypeName: String,
    @PropertyElement val districtCd: Int,
    @PropertyElement val regionName: String,
) {
    fun toBusRouteSearchResultModel() = BusRouteSearchResultModel(
        Id(routeId),
        Utils.getRouteType(routeTypeCd),
        routeName,
        regionName
    )
}