package com.san.busing.data.entity

import android.nfc.FormatException
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteInfoModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * RouteInfo
 *
 * 특정 노선의 상세 정보를 담는 클래스
 * 노선 상세 및 노선 정보 화면 컨텐츠를 구성한다.
 */
@Xml(name = "busRouteInfoItem")
data class RouteInfo(
    // up - 기점, down - 종점
    @PropertyElement val routeId: Int,
    @PropertyElement val routeName: String,
    @PropertyElement val routeTypeCd: Int,
    @PropertyElement val routeTypeName: String,
    @PropertyElement val startStationId: Int,
    @PropertyElement val startStationName: String,
    @PropertyElement val endStationId: Int,
    @PropertyElement val endStationName: String,
    @PropertyElement(name = "upFirstTime") val startFirstTime: String?,
    @PropertyElement(name = "upLastTime") val startLastTime: String?,
    @PropertyElement(name = "downFirstTime") val endFirstTime: String?,
    @PropertyElement(name = "downLastTime") val endLastTime: String?,
    @PropertyElement(name = "nPeekAlloc") val maxPeekAlloc: Int?
) {
    fun toRouteInfoModel() = RouteInfoModel(
        Id(routeId),
        routeName,
        Id(startStationId),
        startStationName,
        Id(endStationId),
        endStationName,
        localTime(startFirstTime),
        localTime(startLastTime),
        localTime(endFirstTime),
        localTime(endLastTime),
        maxPeekAlloc
    )

    private fun localTime(time: String?): LocalTime? {
        if (time == null) return null

        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: FormatException) {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"))
        } catch (e: FormatException) {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:m"))
        } catch (e: FormatException) {
            throw Exception(ExceptionMessage.WRONG_TIME_FORMAT_EXCEPTION)
        }
    }
}

// RouteInfo Path 어노테이션 중복 입력을 최소화하기 위한 클래스
@Xml
data class RouteInfoItem(
    @Path("msgBody") @Element val item: RouteInfo
) {
    fun get(): RouteInfoModel {
        return item.toRouteInfoModel()
    }
}