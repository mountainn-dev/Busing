package com.san.busing.domain.model

import com.san.busing.data.entity.BusRouteRecentSearch
import com.san.busing.data.type.Id
import java.io.Serializable

data class BusRouteRecentSearchModel(
    val id: Id,
    val name: String
) : Serializable {
    fun toBusRouteRecentSearchEntity() = BusRouteRecentSearch(
        id.get(),
        name
    )
}
