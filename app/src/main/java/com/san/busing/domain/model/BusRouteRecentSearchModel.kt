package com.san.busing.domain.model

import com.san.busing.data.entity.BusRouteRecentSearch
import com.san.busing.data.vo.Id
import java.io.Serializable

data class BusRouteRecentSearchModel(
    val id: Id,
    val name: String,
    val index: Long
) : Serializable {
    fun toBusRouteRecentSearchEntity() = BusRouteRecentSearch(
        id.get(),
        name,
        index
    )
}
