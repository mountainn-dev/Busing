package com.san.busing.domain.modelimpl

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.RouteModel
import java.io.Serializable
import java.time.LocalTime

/**
 * RouteInfoModel
 *
 * 특정 노선의 상세 정보를 담는 클래스
 * 버스 노선 상세 화면 컨텐츠를 구성한다.
 */
data class RouteInfoModel(
    override val id: Id,
    override val type: RouteType,
    override val name: String,
    override val regionName: String,
    val startStationId: Id,
    val startStationName: String,
    val endStationId: Id,
    val endStationName: String,
    val startFirstTime: LocalTime?,
    val startLastTime: LocalTime?,
    val endFirstTime: LocalTime?,
    val endLastTime: LocalTime?,
    val maxPeekAlloc: Int
) : RouteModel {
    override fun isSame(id: Id) = this.id == id
}
