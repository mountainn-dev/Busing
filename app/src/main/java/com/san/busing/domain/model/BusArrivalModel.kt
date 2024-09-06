package com.san.busing.domain.model

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.ArrivalFlag

/**
 * BusArrivalModel
 *
 * 정류장 버스 도착 정보를 담는 클래스
 * 정류장 상세 화면의 버스 도착 정보 컨텐츠를 구성한다.
 */
class BusArrivalModel(
    val id: Id,
    val locationFirst: Int,
    val locationSecond: Int,
    val predictTimeFirst: Int,
    val predictTimeSecond: Int,
    val isLowPlateFirst: Boolean,
    val isLowPlateSecond: Boolean,
    val plateNoFirst: String,
    val plateNoSecond: String,
    val remainSeatFirst: Int,
    val remainSeatSecond: Int,
    val sequenceNumber: Int,
    val arrivalFlag: ArrivalFlag
) {
    fun nextStationSeq() = sequenceNumber + 1
}

data class BusArrivalModels(
    private val models: List<BusArrivalModel>
) {
    fun get(index: Int) = models[index]
    fun count() = models.size
}