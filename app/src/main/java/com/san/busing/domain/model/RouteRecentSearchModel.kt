package com.san.busing.domain.model

import com.san.busing.data.entity.RouteRecentSearch
import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType

/**
 * RouteRecentSearchModel
 *
 * 최근 검색한 버스 노선 정보를 담는 클래스
 * 버스 노선 검색 화면 상단에 위치한 최근 검색 아이템을 구성한다.
 */
data class RouteRecentSearchModel(
    val id: Id,
    val name: String,
    val type: RouteType,
    val index: Long
) {
    fun toBusRouteRecentSearchEntity() = RouteRecentSearch(
        id.get(),
        name,
        type.code,
        index
    )
}