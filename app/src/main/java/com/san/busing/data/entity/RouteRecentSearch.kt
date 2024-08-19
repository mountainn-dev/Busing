package com.san.busing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.utils.Utils

/**
 * RouteRecentSearch
 *
 * 최근 검색한 노선 정보를 담는 클래스
 * 노선 검색 화면 상단에 위치한 최근 검색 아이템을 구성한다.
 *
 * bookMark: 즐겨찾기 상태 데이터, SearchRouteFragment 에서 아이템 상단 고정 여부에 사용된다.
 */
@Entity
data class RouteRecentSearch(
    @PrimaryKey val id: Int,
    val routeName: String,
    val routeTypeCd: Int,
    val index: Long,
    val bookMark: Boolean
) {
    fun toRouteRecentSearchModel() = RouteRecentSearchModel(
        Id(id),
        routeName,
        Utils.getRouteType(routeTypeCd),
        index,
        bookMark
    )
}
