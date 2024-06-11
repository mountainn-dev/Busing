package com.san.busing.domain.model

import com.san.busing.data.entity.BusRouteRecentSearch
import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType
import java.io.Serializable

data class BusRouteRecentSearchModel(
    val id: Id,
    val name: String,
    val type: RouteType,
    val index: Long
) {
    fun toBusRouteRecentSearchEntity() = BusRouteRecentSearch(
        id.get(),
        name,
        type.code,
        index
    )
}