package com.san.busing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.san.busing.data.ExceptionMessage
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.enums.RouteType.*

/**
 * BusRouteRecentSearch
 *
 * 최근 검색한 버스 노선 정보를 담는 클래스
 * 버스 노선 검색 화면 상단에 위치한 최근 검색 아이템을 구성한다.
 */
@Entity
data class BusRouteRecentSearch(
    @PrimaryKey val id: Int,
    val routeName: String,
    val routeTypeCd: Int,
    val index: Long,
) {
    fun toBusRouteRecentSearchModel() = BusRouteRecentSearchModel(
        Id(id),
        routeName,
        routeType(routeTypeCd),
        index
    )

    private fun routeType(routeTypeCd: Int) = when (routeTypeCd) {
        AIRPORT_NORMAL.code -> AIRPORT_NORMAL
        AIRPORT_LIMO.code -> AIRPORT_LIMO
        AIRPORT_SEAT.code -> AIRPORT_SEAT
        AREA_DIRECT.code -> AREA_DIRECT
        AREA_EXPRESS.code -> AREA_EXPRESS
        CIRCULAR.code -> CIRCULAR
        NORMAL.code -> NORMAL
        NORMAL_SEAT.code -> NORMAL_SEAT
        OUT_TOWN_NORMAL.code -> OUT_TOWN_NORMAL
        OUT_TOWN_EXPRESS.code -> OUT_TOWN_EXPRESS
        OUT_TOWN_SEAT.code -> OUT_TOWN_SEAT
        RURAL_NORMAL.code -> RURAL_NORMAL
        RURAL_DIRECT.code -> RURAL_DIRECT
        RURAL_SEAT.code -> RURAL_SEAT
        VILLAGE.code -> VILLAGE
        else -> throw IllegalArgumentException(ExceptionMessage.NO_ROUTE_TYPE_EXCEPTION)
    }
}
