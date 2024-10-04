package com.san.busing.domain.modelimpl.route

import com.san.busing.data.entity.route.RouteRecentSearch
import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.route.RouteModel

/**
 * RouteRecentSearchModel
 *
 * 최근 검색한 버스 노선 정보를 담는 클래스
 * 버스 노선 검색 화면 상단에 위치한 최근 검색 아이템을 구성한다.
 */
data class RouteRecentSearchModel(
    override val id: Id,
    override val type: RouteType,
    override val name: String,
    override val regionName: String,
    val index: Long,
    val bookMark: Boolean,
) : RouteModel {
    override fun isSame(id: Id) = this.id == id

    fun toRouteRecentSearchEntity() =
        RouteRecentSearch(
            id.get(),
            type.code,
            name,
            regionName,
            index,
            bookMark,
        )
}

data class RouteRecentSearchModels(
    private val data: List<RouteRecentSearchModel>,
) {
    private val models =
        data.sortedWith(
            compareByDescending<RouteRecentSearchModel> { it.bookMark }.thenByDescending { it.index },
        )

    fun get(index: Int) = models[index]

    fun count() = models.size

    fun isEmpty() = models.isEmpty()

    companion object {
        fun instance() = RouteRecentSearchModels(listOf())
    }
}
