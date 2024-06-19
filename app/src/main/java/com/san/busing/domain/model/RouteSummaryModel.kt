package com.san.busing.domain.model

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType

/**
 * RouteSummaryModel
 *
 * 버스 노선 검색 시 노선 요약 정보를 담는 클래스
 * 버스 노선 검색 화면 검색 결과를 구성한다.
 */
data class RouteSummaryModel(
    val id: Id,
    val type: RouteType,
    val name: String,
    val region: String
)