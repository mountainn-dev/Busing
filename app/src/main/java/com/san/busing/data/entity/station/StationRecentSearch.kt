package com.san.busing.data.entity.station

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.station.StationRecentSearchModel

/**
 * StationRecentSearch
 *
 * 최근 검색한 정류장 정보를 담는 클래스
 * 정류장 검색 화면 상단에 위치한 최근 검색 아이템을 구성한다.
 *
 * bookMark: 즐겨찾기 상태 데이터, SearchStationFragment 에서 아이템 상단 고정 여부에 사용된다.
 */
@Entity
data class StationRecentSearch(
    @PrimaryKey val id: Int,
    val mobileNo: String,
    val name: String,
    val regionName: String,
    val index: Long,
    val bookMark: Boolean
) {
    fun toStationRecentSearchModel() = StationRecentSearchModel(
        Id(id),
        mobileNo,
        name,
        regionName,
        index,
        bookMark
    )
}
