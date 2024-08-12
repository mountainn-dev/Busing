package com.san.busing.domain.model

import com.san.busing.data.vo.Id
import java.io.Serializable
import java.time.LocalTime

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
    val startFirstTime: LocalTime?,
    val startLastTime: LocalTime?,
    val endFirstTime: LocalTime?,
    val endLastTime: LocalTime?,
    val maxPeekAlloc: Int?
) : Serializable
