package com.san.busing.data.entity.route

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.route.RouteRecentSearchModel
import com.san.busing.domain.utils.Utils

/**
 * RouteRecentSearch
 *
 * 최근 검색한 노선 정보를 담는 클래스
 * 노선 검색 화면 상단에 위치한 최근 검색 아이템을 구성한다.
 *
 * bookMark: 즐겨찾기 상태 데이터, SearchRouteFragment 에서 아이템 상단 고정 여부에 사용된다.
 * 북마크 데이터 연동 작업이 검색 화면에서도 진행되어 편의성을 위해 북마크 데이터를 분리하여 유지하지 않고 엔티티와 함께 묶었다.
 */
@Entity
data class RouteRecentSearch(
    @PrimaryKey val id: Int,
    val typeCd: Int,
    val name: String,
    val regionName: String,
    val index: Long,
    val bookMark: Boolean
) {
    fun toRouteRecentSearchModel() = RouteRecentSearchModel(
        Id(id),
        Utils.getRouteType(typeCd),
        name,
        regionName,
        index,
        bookMark
    )
}
