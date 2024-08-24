package com.san.busing.domain.model

import com.san.busing.data.entity.StationRecentSearch
import com.san.busing.data.vo.Id

/**
 * StationRecentSearchModel
 *
 * 최근 검색한 정류장 정보를 담는 클래스
 * 정류장 검색 화면 상단에 위치한 최근 검색 아이템을 구성한다.
 */
data class StationRecentSearchModel(
    val id: Id,
    val mobileNo: String,
    val name: String,
    val regionName: String,
    val index: Long,
    val bookMark: Boolean
) {
    fun toStationRecentSearchEntity() = StationRecentSearch(
        id.get(),
        mobileNo,
        name,
        regionName,
        index,
        bookMark
    )
}
