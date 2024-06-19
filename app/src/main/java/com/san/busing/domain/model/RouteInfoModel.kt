package com.san.busing.domain.model

import com.san.busing.data.vo.Id

/**
 * RouteInfoModel
 *
 * 특정 노선의 상세 정보를 담는 클래스
 * 버스 노선 상세 화면 컨텐츠를 구성한다.
 */
data class RouteInfoModel(
    val id: Id,
    val name: String,
    val startStationId: Id,
    val startStationName: String,
    val endStationId: Id,
    val endStationName: String,
//    val startFirstTime: Time,
//    val startLastTime: Time,
//    val finishFirstTime: Time,
//    val finishLastTime: Time,
//    val maxPeekAlloc: Time
)
