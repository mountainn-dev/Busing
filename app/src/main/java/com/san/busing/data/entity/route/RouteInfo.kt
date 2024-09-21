package com.san.busing.data.entity.route

import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.route.RouteInfoModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * RouteInfo
 *
 * 노선의 상세 정보를 담는 클래스
 * 노선 상세 및 노선 정보 화면 컨텐츠를 구성한다.
 */
@Xml(name = "busRouteInfoItem")
data class RouteInfo(
    // up - 기점, down - 종점
    @PropertyElement(name = "routeId") val id: Int,
    @PropertyElement(name = "routeTypeCd") val typeCd: Int,
    @PropertyElement(name = "routeName") val routeName: String,
    @PropertyElement val regionName: String?,
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
        Id(id),
        Utils.getRouteType(typeCd),
        routeName,
        regionName(regionName),
        Id(startStationId),
        startStationName,
        Id(endStationId),
        endStationName,
        localTime(startFirstTime),
        localTime(startLastTime),
        localTime(endFirstTime),
        localTime(endLastTime),
        maxPeekAlloc ?: Const.ZERO
    )

    private fun regionName(name: String?) = when(name != null) {
        true -> name
        false -> Const.EMPTY_TEXT
    }

    private fun localTime(time: String?): LocalTime? {
        if (time == null) return null

        return try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: DateTimeParseException) {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"))
        } catch (e: DateTimeParseException) {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:m"))
        } catch (e: DateTimeParseException) {
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