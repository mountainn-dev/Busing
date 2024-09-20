package com.san.busing.domain.modelimpl

import com.san.busing.data.entity.StationRecentSearch
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.StationModel

/**
 * StationRecentSearchModel
 *
 * 최근 검색한 정류장 정보를 담는 클래스
 * 정류장 검색 화면 상단에 위치한 최근 검색 아이템을 구성한다.
 */
data class StationRecentSearchModel(
    override val id: Id,
    override val mobileNo: String,
    override val name: String,
    override val regionName: String,
    val index: Long,
    val bookMark: Boolean
) : StationModel {
    override fun isSame(id: Id) = this.id == id

    fun toStationRecentSearchEntity() = StationRecentSearch(
        id.get(),
        mobileNo,
        name,
        regionName,
        index,
        bookMark
    )
}

data class StationRecentSearchModels(
    private val data: List<StationRecentSearchModel>
) {
    private val models = data.sortedWith(
        compareByDescending<StationRecentSearchModel> { it.bookMark }.thenByDescending { it.index })

    fun get(index: Int) = models[index]
    fun count() = models.size
    fun isEmpty() = models.isEmpty()

    companion object {
        fun instance() = StationRecentSearchModels(listOf())
    }
}
