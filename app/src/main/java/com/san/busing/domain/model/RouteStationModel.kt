package com.san.busing.domain.model

import com.san.busing.data.vo.Id

/**
 * RouteStationModel
 *
 * 노선 상세 화면의 정류장 아이템 정보를 담는 클래스
 * 버스 노선 상세 화면 컨텐츠를 구성한다.
 */
data class RouteStationModel(
    val id: Id,
    val number: Int,
    val name: String,
    val sequenceNumber: Int,
    val isTurnaround: Boolean,
    val positionX: Double,
    val positionY: Double
)
