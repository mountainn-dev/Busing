package com.san.busing.domain.modelimpl

import com.san.busing.data.entity.BusRouteRecentSearch
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RecentSearchModel
import java.io.Serializable

data class BusRouteRecentSearchModelImpl(
    override val id: Id,
    override val name: String,
    override val index: Long
) : Serializable, RecentSearchModel {
    fun toBusRouteRecentSearchEntity() = BusRouteRecentSearch(
        id.get(),
        name,
        index
    )
}