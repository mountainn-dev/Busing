package com.san.busing.data.entity

import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.ArrivalFlag
import com.san.busing.domain.modelimpl.BusArrivalModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * BusArrival
 *
 * 정류장 버스 도착 정보를 담는 클래스
 * 정류장 상세 화면의 버스 도착 정보 컨텐츠를 구성한다.
 */
@Xml(name="busArrivalItem")
data class BusArrival(
    @PropertyElement val stationId: Int,
    @PropertyElement val routeId: Int,
    @PropertyElement(name = "locationNo1") val locationFirst: Int,
    @PropertyElement(name = "locationNo2") val locationSecond: Int,
    @PropertyElement(name = "predictTime1") val predictTimeFirst: Int,
    @PropertyElement(name = "predictTime2") val predictTimeSecond: Int,
    @PropertyElement(name = "lowPlate1") val lowPlateFirst: Int,
    @PropertyElement(name = "lowPlate2") val lowPlateSecond: Int,
    @PropertyElement(name = "plateNo1") val plateNoFirst: String,
    @PropertyElement(name = "plateNo2") val plateNoSecond: String,
    @PropertyElement(name = "remainSeatCnt1") val remainSeatCntFirst: Int,
    @PropertyElement(name = "remainSeatCnt2") val remainSeatCntSecond: Int,
    @PropertyElement(name = "staOrder") val stationSeq: Int,
    @PropertyElement val flag: String
) {
    fun toBusArrivalModel() = BusArrivalModel(
        Id(routeId),
        locationFirst,
        locationSecond,
        predictTimeFirst,
        predictTimeSecond,
        isLowPlate(lowPlateFirst),
        isLowPlate(lowPlateSecond),
        plateNoFirst,
        plateNoSecond,
        remainSeatCntFirst,
        remainSeatCntSecond,
        stationSeq,
        ArrivalFlag.find(flag)
    )

    private fun isLowPlate(lowPlate: Int) = when(lowPlate) {
        0 -> false
        1 -> true
        else -> throw Exception(ExceptionMessage.WRONG_LOW_PLATE_VALUE_EXCEPTION)
    }
}

@Xml
data class BusArrivalItem(
    @Path("msgBody") @Element val item: BusArrival
) {
    fun get(): BusArrivalModel {
        return item.toBusArrivalModel()
    }
}
