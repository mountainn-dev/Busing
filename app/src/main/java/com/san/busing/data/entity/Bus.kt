package com.san.busing.data.entity

import com.san.busing.data.ExceptionMessage
import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.PlateType.COMPACT
import com.san.busing.domain.enums.PlateType.DOUBLE_DECKER
import com.san.busing.domain.enums.PlateType.FULL_SIZE
import com.san.busing.domain.enums.PlateType.MID_SIZE
import com.san.busing.domain.model.BusModel
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * Bus
 *
 * 노선별 실시간 운행중인 버스 정보를 담는 클래스
 * 노선 상세 화면의 버스 위치 컨텐츠를 구성한다.
 */
@Xml(name = "busLocationList")
data class Bus(
    @PropertyElement val routeId: Int,
    @PropertyElement val stationId: Int,
    @PropertyElement val stationSeq: Int,
    @PropertyElement val endBus: Int,
    @PropertyElement val lowPlate: Int,
    @PropertyElement val plateNo: String,
    @PropertyElement val plateType: Int,
    @PropertyElement val remainSeatCnt: Int
) {
    fun toBusModel() = BusModel(
        Id(routeId),
        plateType(plateType),
        plateNo,
        stationSeq,
        isLast(endBus),
        isLowPlate(lowPlate),
        remainSeat(remainSeatCnt),
    )

    private fun plateType(plateType: Int) = when(plateType) {
        COMPACT.code -> COMPACT
        MID_SIZE.code -> MID_SIZE
        FULL_SIZE.code -> FULL_SIZE
        DOUBLE_DECKER.code -> DOUBLE_DECKER
        else -> throw Exception(ExceptionMessage.NO_PLATE_TYPE_EXCEPTION)
    }

    private fun isLast(endBus: Int) = when(endBus) {
        0 -> false
        1 -> true
        else -> throw Exception(ExceptionMessage.WRONG_END_BUS_VALUE_EXCEPTION)
    }

    private fun isLowPlate(lowPlate: Int) = when(lowPlate) {
        0 -> false
        1 -> true
        else -> throw Exception(ExceptionMessage.WRONG_LOW_PLATE_VALUE_EXCEPTION)
    }

    private fun remainSeat(remainSeatCnt: Int) = when(remainSeatCnt >= 0) {
        true -> remainSeatCnt
        else -> throw Exception(ExceptionMessage.WRONG_REMAIN_SEAT_VALUE_EXCEPTION)
    }
}