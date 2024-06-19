package com.san.busing.domain.model

import com.san.busing.data.entity.RouteRecentSearch
import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType

data class RouteRecentSearchModel(
    val id: Id,
    val name: String,
    val type: RouteType,
    val index: Long
) {
    fun toBusRouteRecentSearchEntity() = RouteRecentSearch(
        id.get(),
        name,
        type.code,
        index
    )
}